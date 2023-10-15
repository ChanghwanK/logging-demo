package com.example.demologging.support.http.logging.aop

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger { }

data class ReqResLogging(
    @JsonProperty(value = "traceId")
    val traceId: String,
    @JsonProperty(value = "class")
    val className: String? = null,
    @JsonProperty(value = "http_method")
    val httpMethod: String? = null,
    @JsonProperty(value = "uri")
    val uri: String? = null,
    @JsonProperty(value = "method")
    val method: String? = null,
    @JsonProperty(value = "params")
    val params: Map<String, Any>? = null,
    @JsonProperty(value = "log_time")
    val logTime: String,
    @JsonProperty(value = "server_ip")
    val serverIp: String? = null,
    @JsonProperty(value = "device_type")
    val deviceType: String? = null,
    @JsonProperty(value = "request_body")
    val requestBody: Any? = null,
    @JsonProperty(value = "response_body")
    val responseBody: Any? = null,
    @JsonProperty(value = "elapsed_time")
    val elapsedTime: String? = null,
)

@Aspect
@Component
class ReqResAspect(
    val objectMapper: ObjectMapper,
) {

    @Pointcut(value = "within(com.example.demologging.api.*)")
    private fun apiPointCut() {
    }

    @Around("apiPointCut()")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        val request =
            (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val traceId = MDC.get("tx-id")

        val className = joinPoint.signature.declaringTypeName // 1.
        val methodName = joinPoint.signature.name // 2.
        val params = getParams(request) // 3.

        val deviceType = request.getHeader("x-custom-device-type")
        val serverIp = InetAddress.getLocalHost().hostAddress

        val reqResLogging = ReqResLogging(
            traceId = traceId,
            className = className,
            httpMethod = request.method,
            uri = request.requestURI,
            method = methodName,
            params = params,
            logTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            serverIp = serverIp,
            deviceType = deviceType,
            requestBody = objectMapper.readTree(request.inputStream.readBytes()),
        )

        logger.info { " \n Request Data: ${objectMapper.writeValueAsString(reqResLogging)}" }

        val res = joinPoint.proceed()

        when (res) {
            is ResponseEntity<*> -> logger.info { "\n body: ${res.body}" }
        }

        return res
    }
}

private fun getParams(request: HttpServletRequest): Map<String, String> {
    val jsonObject = mutableMapOf<String, String>()
    val paramNames = request.parameterNames
    while (paramNames.hasMoreElements()) {
        val paramName = paramNames.nextElement()
        val replaceParam = paramName.replace("\\.", "-")
        jsonObject[replaceParam] = request.getParameter(paramName)
    }
    return jsonObject
}

class CustomException(status: HttpStatus, code: Int, traceId: String, message: String)

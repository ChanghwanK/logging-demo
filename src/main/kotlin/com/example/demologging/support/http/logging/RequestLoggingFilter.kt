package com.example.demologging.support.http.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "Request Log Filter", urlPatterns = ["/*"])
class RequestLoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        // RequestFacade가 들어오고 request는 CoyoteInputStream임
//        logger.info("Request Data: ${objectMapper.readTree(request.inputStream)}")
        val uuid = UUID.randomUUID()
        MDC.put("tx-id", uuid.toString())
        val cachedRequest = RequestWrapper(request)
        filterChain.doFilter(cachedRequest, response)
    }
}

package com.example.demologging

import com.example.demologging.api.InvalidRequestException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.NestedExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = [RuntimeException::class])
    fun handleRunTimeException(e: RuntimeException): ResponseEntity<String> {
        logger.error {
            "\n {\n cause = ${NestedExceptionUtils.getRootCause(e)?.message}, " +
                "\n errorMsg = ${NestedExceptionUtils.getMostSpecificCause(e).message}, " +
                "\n stackTrace = ${e.stackTrace[0]} \n }"
        }
        return ResponseEntity.internalServerError().body("Internal Server Error")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [InvalidRequestException::class])
    fun handleInvalidRequest(e: InvalidRequestException) {
        logger.warn { e }
    }
}

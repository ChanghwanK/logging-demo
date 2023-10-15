package com.example.demologging

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.NestedExceptionUtils
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger { }

@Component
class LoggerSupporter {

    fun war(e: Exception) {
        logger.warn { "cause = ${NestedExceptionUtils.getRootCause(e)}, errorMsg = ${NestedExceptionUtils.getMostSpecificCause(e).message}, stackTrace = ${NestedExceptionUtils.getMostSpecificCause(e).printStackTrace()}" }
    }

    fun error(e: Exception) {
        logger.error { "cause = ${NestedExceptionUtils.getRootCause(e)}, errorMsg = ${NestedExceptionUtils.getMostSpecificCause(e).message}, stackTrace = ${NestedExceptionUtils.getMostSpecificCause(e).printStackTrace()}" }
    }
}

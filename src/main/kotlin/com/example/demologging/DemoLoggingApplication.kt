package com.example.demologging

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.slf4j.logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class DemoLoggingApplication

fun main(args: Array<String>) {
    runApplication<DemoLoggingApplication>(*args)
}

package com.example.demologging.api

import com.example.demologging.HelloService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

data class UserMessage(val msg: String?)

@RestController
class HelloWorldController(
    private val service: HelloService,
) {

    @GetMapping("/api/log")
    fun loggerTestMethod() {
        service.errorRaise()
    }

    @PostMapping("/api/log")
    fun loggerTestPost(
        @RequestBody msg: UserMessage,
    ): ResponseEntity<UserMessage> {
        logger.info { "user message: $msg" }

        return ResponseEntity.ok(msg)
    }

    @PutMapping("/api/log")
    fun loggerTestPut(
        @RequestBody msg: UserMessage,
    ) {
        println("PUT Method")
        throw InvalidRequestException("Invalid Request Error")
    }
}

class InvalidRequestException(msg: String) : RuntimeException(msg)

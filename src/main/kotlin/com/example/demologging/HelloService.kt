package com.example.demologging

import org.springframework.stereotype.Service

class LoggErrorException(private val msg: String) : RuntimeException(msg)

@Service
class HelloService {

    fun errorRaise() {
        throw LoggErrorException("Logging Error")
    }
}

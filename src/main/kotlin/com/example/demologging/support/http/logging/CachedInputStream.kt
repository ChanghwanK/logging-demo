package com.example.demologging.support.http.logging

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream

class CachedInputStream(request: ByteArray) : ServletInputStream() {
    private val cachedBody: InputStream

    init {
        cachedBody = ByteArrayInputStream(request)
    }

    override fun read(): Int = cachedBody.read()

    override fun isFinished(): Boolean = cachedBody.available() == 0

    override fun isReady(): Boolean = true

    override fun setReadListener(listener: ReadListener?) {
        TODO("Not yet implemented")
    }
}

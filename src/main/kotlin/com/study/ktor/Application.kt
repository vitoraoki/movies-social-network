package com.study.ktor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.study.ktor.plugins.*
import io.ktor.application.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

@ExperimentalSerializationApi
private fun Application.module() {
    configureSerialization()
    configureSockets()
    configureRouting()
}


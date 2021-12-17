package com.study.ktor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.study.ktor.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSockets()
        configureSerialization()
    }.start(wait = true)
}

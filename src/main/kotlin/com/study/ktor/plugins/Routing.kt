package com.study.ktor.plugins

import com.study.ktor.plugins.route.usersRoute
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        usersRoute()
    }
}

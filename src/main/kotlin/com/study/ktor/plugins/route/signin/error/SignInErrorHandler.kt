package com.study.ktor.plugins.route.signin.error

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

internal suspend fun PipelineContext<Unit, ApplicationCall>.wrongUserSignInBody() {
    call.respond(
        status = HttpStatusCode.BadRequest,
        message = "Error to SignIn user"
    )
}

internal suspend fun PipelineContext<Unit, ApplicationCall>.userNotSignedUp() {
    call.respond(
        status = HttpStatusCode.ExpectationFailed,
        message = "User not signed up"
    )
}

internal suspend fun PipelineContext<Unit, ApplicationCall>.wrongPassword() {
    call.respond(
        status = HttpStatusCode.BadRequest,
        message = "Wrong password"
    )
}

internal suspend fun PipelineContext<Unit, ApplicationCall>.signInFailed() {
    call.respond(
        status = HttpStatusCode.ExpectationFailed,
        message = "Error to SignIn user"
    )
}
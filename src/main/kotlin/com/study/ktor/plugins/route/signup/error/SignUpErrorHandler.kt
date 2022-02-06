package com.study.ktor.plugins.route.signup.error

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

internal suspend fun PipelineContext<Unit, ApplicationCall>.wrongUserSignUpBody() {
    call.respond(
        status = HttpStatusCode.BadRequest,
        message = "Error to SignUp user"
    )
}

internal suspend fun PipelineContext<Unit, ApplicationCall>.userAlreadySignedUp() {
    call.respond(
        status = HttpStatusCode.Conflict,
        message = "User already signed up"
    )
}

internal suspend fun PipelineContext<Unit, ApplicationCall>.signUpFailed() {
    call.respond(
        status = HttpStatusCode.ExpectationFailed,
        message = "Error to SignUp user"
    )
}
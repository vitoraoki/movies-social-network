package com.study.ktor.plugins.route.session.error

import com.study.ktor.plugins.route.session.result.SessionResult
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

internal suspend fun PipelineContext<Unit, ApplicationCall>.sessionExpired(
    sessionExpired: SessionResult.SessionExpired
) {
    call.respond(
        status = HttpStatusCode.Unauthorized,
        message = sessionExpired.message
    )
}

internal suspend fun PipelineContext<Unit, ApplicationCall>.invalidToken(
    invalidToken: SessionResult.InvalidToken
) {
    call.respond(
        status = HttpStatusCode.Unauthorized,
        message = invalidToken.message
    )
}

internal suspend fun PipelineContext<Unit, ApplicationCall>.sessionNotFound(
    sessionNotFound: SessionResult.SessionNotFound
) {
    call.respond(
        status = HttpStatusCode.Unauthorized,
        message = sessionNotFound.message
    )
}
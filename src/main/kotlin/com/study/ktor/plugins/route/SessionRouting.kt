package com.study.ktor.plugins.route

import com.study.ktor.plugins.result.SessionResult
import com.study.ktor.plugins.route.model.UserSession
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleCreateSessionResult(
    userId: Int,
    createSessionResult: SessionResult
) {
    when(createSessionResult) {
        is SessionResult.CreateSessionSuccess -> handleCreateSessionSuccess(userId, createSessionResult)
        is SessionResult.CreateSessionFailed -> signupFailed()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleCreateSessionSuccess(
    userId: Int,
    createSessionResult: SessionResult.CreateSessionSuccess
) {
    val userSession = UserSession(
        userId = userId.toString(),
        token = createSessionResult.token,
        expiresAt = createSessionResult.expiresAt
    )
    call.respond(status = HttpStatusCode.Created, message = userSession)
}
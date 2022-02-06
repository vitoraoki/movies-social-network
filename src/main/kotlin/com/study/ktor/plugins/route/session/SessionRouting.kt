package com.study.ktor.plugins.route.session

import com.study.ktor.plugins.route.session.model.UserSession
import com.study.ktor.plugins.route.session.result.SessionResult
import com.study.ktor.plugins.route.signin.error.signInFailed
import com.study.ktor.plugins.route.signup.error.signUpFailed
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleSessionResult(
    userId: Int,
    sessionResult: SessionResult
) {
    when(sessionResult) {
        is SessionResult.CreateSessionSuccess -> handleCreateSessionSuccess(userId, sessionResult)
        is SessionResult.CreateSessionFailed -> signUpFailed()
        is SessionResult.UpdateSessionSuccess -> handleUpdateSessionSuccess(userId, sessionResult)
        is SessionResult.UpdateSessionFailed -> signInFailed()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleUpdateSessionSuccess(
    userId: Int,
    updateSessionSuccess: SessionResult.UpdateSessionSuccess
) {
    handleSessionSuccess(
        userId = userId,
        token = updateSessionSuccess.token,
        expiresAt = updateSessionSuccess.expiresAt
    )
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleCreateSessionSuccess(
    userId: Int,
    createSessionResult: SessionResult.CreateSessionSuccess
) {
    handleSessionSuccess(
        userId = userId,
        token = createSessionResult.token,
        expiresAt = createSessionResult.expiresAt
    )
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleSessionSuccess(
    userId: Int,
    token: String,
    expiresAt: Long
) {
    val userSession = UserSession(
        userId = userId.toString(),
        token = token,
        expiresAt = expiresAt
    )
    call.respond(status = HttpStatusCode.Created, message = userSession)
}
package com.study.ktor.plugins.route.session

import com.study.ktor.plugins.route.session.error.invalidToken
import com.study.ktor.plugins.route.session.error.sessionExpired
import com.study.ktor.plugins.route.session.error.sessionNotFound
import com.study.ktor.plugins.route.session.model.SessionResponse
import com.study.ktor.plugins.route.session.model.SessionValidateData
import com.study.ktor.plugins.route.session.result.SessionResult
import com.study.ktor.plugins.route.signin.error.signInFailed
import com.study.ktor.plugins.route.signup.error.signUpFailed
import com.study.ktor.repository.SessionsRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.validateSession() {
    val sessionValidateData = call.receive<SessionValidateData>()
    val result = SessionsRepository().validateSession(sessionValidateData = sessionValidateData)
    handleSessionResult(userId = sessionValidateData.userId, sessionResult = result)
}

suspend fun PipelineContext<Unit, ApplicationCall>.handleSessionResult(
    userId: Int,
    sessionResult: SessionResult
) {
    when(sessionResult) {
        is SessionResult.CreateSessionSuccess -> handleCreateSessionSuccess(userId, sessionResult)
        is SessionResult.CreateSessionFailed -> signUpFailed()
        is SessionResult.UpdateSessionSuccess -> handleUpdateSessionSuccess(userId, sessionResult)
        is SessionResult.UpdateSessionFailed -> signInFailed()
        is SessionResult.ValidSession -> handleValidSession()
        is SessionResult.SessionExpired -> sessionExpired(sessionExpired = sessionResult)
        is SessionResult.InvalidToken -> invalidToken(invalidToken = sessionResult)
        is SessionResult.SessionNotFound -> sessionNotFound(sessionNotFound = sessionResult)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleValidSession() {
    call.respond(HttpStatusCode.OK)
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
    val sessionResponse = SessionResponse(
        userId = userId,
        token = token,
        expiresAt = expiresAt
    )
    call.respond(status = HttpStatusCode.Created, message = sessionResponse)
}
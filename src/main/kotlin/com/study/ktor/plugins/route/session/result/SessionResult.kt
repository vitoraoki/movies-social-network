package com.study.ktor.plugins.route.session.result

import com.study.ktor.plugins.route.ErrorMessages

sealed class SessionResult {
    data class CreateSessionSuccess(
        val token: String,
        val expiresAt: Long
    ): SessionResult()

    data class UpdateSessionSuccess(
        val token: String,
        val expiresAt: Long
    ): SessionResult()

    object ValidSession: SessionResult()

    object CreateSessionFailed: SessionResult()
    object UpdateSessionFailed: SessionResult()

    data class SessionExpired(
        val message: String = ErrorMessages.SESSION_EXPIRED.value
    ): SessionResult()

    data class InvalidToken(
        val message: String = ErrorMessages.INVALID_TOKEN.value
    ): SessionResult()

    data class SessionNotFound(
        val message: String = ErrorMessages.SESSION_NOT_FOUND.value
    ): SessionResult()
}

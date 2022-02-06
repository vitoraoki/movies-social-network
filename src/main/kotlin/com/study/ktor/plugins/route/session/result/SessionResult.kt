package com.study.ktor.plugins.route.session.result

sealed class SessionResult {
    data class CreateSessionSuccess(
        val token: String,
        val expiresAt: Long
    ): SessionResult()

    object CreateSessionFailed: SessionResult()
}

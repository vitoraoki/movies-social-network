package com.study.ktor.plugins.result

sealed class SessionResult {
    data class CreateSessionSuccess(
        val token: String,
        val expiresAt: Long
    ): SessionResult()

    object CreateSessionFailed: SessionResult()
}

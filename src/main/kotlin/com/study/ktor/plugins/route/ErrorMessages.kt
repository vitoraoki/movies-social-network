package com.study.ktor.plugins.route

enum class ErrorMessages(val value: String) {
    SESSION_EXPIRED("Session expired"),
    INVALID_TOKEN("Invalid token"),
    SESSION_NOT_FOUND("A session for this user was not found")
}
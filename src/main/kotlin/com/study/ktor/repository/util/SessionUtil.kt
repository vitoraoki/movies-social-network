package com.study.ktor.repository.util

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

private const val TOKEN_VALIDITY_IN_DAYS = 5L

object SessionUtil {
    fun generateToken() = UUID.randomUUID().toString()

    fun getExpiresAtDate() = LocalDateTime.now(ZoneOffset.UTC)
        .plusDays(TOKEN_VALIDITY_IN_DAYS)
        .atZone(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()

    fun getCurrentDate() = LocalDateTime.now(ZoneOffset.UTC)
        .atZone(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}
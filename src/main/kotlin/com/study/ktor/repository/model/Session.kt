package com.study.ktor.repository.model

data class Session(
    val id: Int,
    val userId: Int,
    val token: String,
    val expiresAt: Long
)
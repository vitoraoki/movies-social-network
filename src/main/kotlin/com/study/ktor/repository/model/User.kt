package com.study.ktor.repository.model

import com.study.ktor.repository.Cryptography

data class User(
    val id: Int,
    val firstName: String,
    val secondName: String,
    val email: String,
    val password: String
) {
    fun isPasswordCorrect(receivedPassword: String): Boolean = password == Cryptography.sha512(receivedPassword)
}

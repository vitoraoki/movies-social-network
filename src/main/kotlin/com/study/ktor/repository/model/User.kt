package com.study.ktor.repository.model

data class User(
    val id: Int,
    val firstName: String,
    val secondName: String,
    val email: String,
    val password: String
)

package com.study.ktor.plugins.route.signin.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSignIn(
    val email: String,
    val password: String
)

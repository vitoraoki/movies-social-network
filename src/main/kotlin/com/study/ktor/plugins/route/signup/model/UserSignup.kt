package com.study.ktor.plugins.route.signup.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignUp(
    @SerialName("first_name") val firstName: String,
    @SerialName("second_name") val secondName: String,
    val email: String,
    val password: String
)

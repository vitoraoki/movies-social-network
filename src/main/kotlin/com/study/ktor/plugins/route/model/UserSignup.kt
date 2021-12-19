package com.study.ktor.plugins.route.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignup(
    @SerialName("first_name") val firstName: String,
    @SerialName("second_name") val secondName: String,
    val email: String,
    val password: String
)

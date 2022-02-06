package com.study.ktor.plugins.route.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    @SerialName("user_id") val userId: String,
    val token: String,
    @SerialName("expires_at") val expiresAt: Long
)

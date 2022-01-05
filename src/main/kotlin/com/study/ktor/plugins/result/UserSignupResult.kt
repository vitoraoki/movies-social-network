package com.study.ktor.plugins.result

sealed class UserSignupResult {
    data class UserSignupSuccess(val id: Int): UserSignupResult()

    object UserAlreadySignedUp: UserSignupResult()
    object SignupFailed: UserSignupResult()
}
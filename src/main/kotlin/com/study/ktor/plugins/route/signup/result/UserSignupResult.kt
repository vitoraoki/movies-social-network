package com.study.ktor.plugins.route.signup.result

sealed class UserSignUpResult {
    data class UserSignUpSuccess(val id: Int): UserSignUpResult()

    object UserAlreadySignedUp: UserSignUpResult()
    object SignUpFailed: UserSignUpResult()
}
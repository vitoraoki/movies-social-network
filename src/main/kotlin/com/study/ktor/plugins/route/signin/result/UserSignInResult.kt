package com.study.ktor.plugins.route.signin.result

sealed class UserSignInResult {
    data class UserSignInSuccess(val id: Int): UserSignInResult()

    object WrongPassword: UserSignInResult()
    object UserNotSignedUp: UserSignInResult()
}

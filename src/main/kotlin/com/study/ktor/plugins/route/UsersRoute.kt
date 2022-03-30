package com.study.ktor.plugins.route

import com.study.ktor.plugins.route.session.validateSession
import com.study.ktor.plugins.route.signin.signIn
import com.study.ktor.plugins.route.signup.signUp
import io.ktor.routing.*

private const val USER_PATH = "/user"
private const val SIGN_UP_USER_PATH = "/sign_up"
private const val SIGN_IN_USER_PATH = "/sign_in"
private const val SESSION = "/session"
private const val SESSION_VALIDATE = "$SESSION/validate"

fun Route.usersRoute() {
    route(USER_PATH) {
        post(SIGN_UP_USER_PATH) {
            signUp()
        }

        post(SIGN_IN_USER_PATH) {
            signIn()
        }

        post(SESSION_VALIDATE) {
            validateSession()
        }
    }
}
package com.study.ktor.plugins.route

import com.study.ktor.plugins.route.signin.signIn
import com.study.ktor.plugins.route.signup.signUp
import com.study.ktor.repository.UsersRepository
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

private const val USER_PATH = "/user"
private const val ALL_PATH = "all"
private const val SIGN_UP_USER_PATH = "/sign_up"
private const val SIGN_IN_USER_PATH = "/sign_in"

fun Route.usersRoute() {
    route(USER_PATH) {
        get(ALL_PATH) {
            val users = UsersRepository.getAllUsers()
            call.respond(users)
        }

        post(SIGN_UP_USER_PATH) {
            signUp()
        }
        
        post(SIGN_IN_USER_PATH) {
            signIn()
        }
    }
}
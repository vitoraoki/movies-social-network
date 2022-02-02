package com.study.ktor.plugins.route

import com.study.ktor.repository.UsersRepository
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

private const val USER_PATH = "/user"
private const val ALL_PATH = "all"
private const val ID = "id"
private const val ID_PARAMETER = "{$ID}"
private const val SIGNUP_USER_PATH = "/signup"

fun Route.usersRoute() {
    route(USER_PATH) {
        get(ALL_PATH) {
            val users = UsersRepository.getAllUsers()
            call.respond(users)
        }

        post(SIGNUP_USER_PATH) {
            signup()
        }
    }
}


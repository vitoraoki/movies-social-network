package com.study.ktor.plugins.route

import com.study.ktor.plugins.result.UserSignupResult
import com.study.ktor.plugins.route.model.UserSignup
import com.study.ktor.repository.SessionsRepository
import com.study.ktor.repository.UsersRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerializationException

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

private suspend fun PipelineContext<Unit, ApplicationCall>.signup() {
    try {
        val userSignup = call.receive<UserSignup>()
        val result = UsersRepository.signupUser(userSignup)
        handleSignupResult(result)
    } catch (exception: SerializationException) {
        wrongUserSignupBody()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleSignupResult(result: UserSignupResult) {
    when (result) {
        is UserSignupResult.UserSignupSuccess -> {
            SessionsRepository.createSession(result.id)
            call.respond(HttpStatusCode.Created)
        }
        is UserSignupResult.UserAlreadySignedUp -> userAlreadySignedUp()
        is UserSignupResult.SignupFailed -> signupFailed()
    }
}


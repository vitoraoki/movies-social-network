package com.study.ktor.plugins.route

import com.study.ktor.plugins.result.UserSignupResult
import com.study.ktor.plugins.route.model.UserSignup
import com.study.ktor.repository.SessionsRepository
import com.study.ktor.repository.UsersRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerializationException

suspend fun PipelineContext<Unit, ApplicationCall>.signup() {
    try {
        val userSignup = call.receive<UserSignup>()
        val result = UsersRepository.signupUser(userSignup)
        handleSignupResult(result)
    } catch (exception: SerializationException) {
        wrongUserSignupBody()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleSignupResult(userSignupResult: UserSignupResult) {
    when (userSignupResult) {
        is UserSignupResult.UserSignupSuccess -> {
            val createSessionResult = SessionsRepository.createSession(userSignupResult.id)
            handleCreateSessionResult(userSignupResult.id, createSessionResult)
        }
        is UserSignupResult.UserAlreadySignedUp -> userAlreadySignedUp()
        is UserSignupResult.SignupFailed -> signupFailed()
    }
}

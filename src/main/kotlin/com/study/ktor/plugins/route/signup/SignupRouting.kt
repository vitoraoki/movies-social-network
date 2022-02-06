package com.study.ktor.plugins.route.signup

import com.study.ktor.plugins.route.signup.result.UserSignUpResult
import com.study.ktor.plugins.route.session.handleCreateSessionResult
import com.study.ktor.plugins.route.signup.model.UserSignUp
import com.study.ktor.plugins.route.signup.error.signupFailed
import com.study.ktor.plugins.route.signup.error.userAlreadySignedUp
import com.study.ktor.plugins.route.signup.error.wrongUserSignupBody
import com.study.ktor.repository.SessionsRepository
import com.study.ktor.repository.UsersRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerializationException

suspend fun PipelineContext<Unit, ApplicationCall>.signUp() {
    try {
        val userSignup = call.receive<UserSignUp>()
        val result = UsersRepository.signUpUser(userSignup)
        handleSignupResult(result)
    } catch (exception: SerializationException) {
        wrongUserSignupBody()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleSignupResult(userSignupResult: UserSignUpResult) {
    when (userSignupResult) {
        is UserSignUpResult.UserSignUpSuccess -> {
            val createSessionResult = SessionsRepository.createSession(userSignupResult.id)
            handleCreateSessionResult(userSignupResult.id, createSessionResult)
        }
        is UserSignUpResult.UserAlreadySignedUp -> userAlreadySignedUp()
        is UserSignUpResult.SignUpFailed -> signupFailed()
    }
}

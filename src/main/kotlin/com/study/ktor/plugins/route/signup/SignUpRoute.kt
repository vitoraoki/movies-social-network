package com.study.ktor.plugins.route.signup

import com.study.ktor.plugins.route.signup.result.UserSignUpResult
import com.study.ktor.plugins.route.session.handleSessionResult
import com.study.ktor.plugins.route.signup.model.UserSignUp
import com.study.ktor.plugins.route.signup.error.signUpFailed
import com.study.ktor.plugins.route.signup.error.userAlreadySignedUp
import com.study.ktor.plugins.route.signup.error.wrongUserSignUpBody
import com.study.ktor.repository.SessionsRepository
import com.study.ktor.repository.UsersRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerializationException

suspend fun PipelineContext<Unit, ApplicationCall>.signUp() {
    try {
        val userSignup = call.receive<UserSignUp>()
        val result = UsersRepository().signUpUser(userSignup)
        handleSignupResult(result)
    } catch (exception: SerializationException) {
        wrongUserSignUpBody()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleSignupResult(userSignupResult: UserSignUpResult) {
    when (userSignupResult) {
        is UserSignUpResult.UserSignUpSuccess -> {
            val createSessionResult = SessionsRepository().createSession(userSignupResult.id)
            handleSessionResult(userId = userSignupResult.id, sessionResult = createSessionResult)
        }
        is UserSignUpResult.UserAlreadySignedUp -> userAlreadySignedUp()
        is UserSignUpResult.SignUpFailed -> signUpFailed()
    }
}

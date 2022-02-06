package com.study.ktor.plugins.route.signin

import com.study.ktor.plugins.route.session.handleSessionResult
import com.study.ktor.plugins.route.signin.error.userNotSignedUp
import com.study.ktor.plugins.route.signin.error.wrongPassword
import com.study.ktor.plugins.route.signin.error.wrongUserSignInBody
import com.study.ktor.plugins.route.signin.model.UserSignIn
import com.study.ktor.plugins.route.signin.result.UserSignInResult
import com.study.ktor.repository.SessionsRepository
import com.study.ktor.repository.UsersRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerializationException

suspend fun PipelineContext<Unit, ApplicationCall>.signIn() {
    try {
        val userSignIn = call.receive<UserSignIn>()
        val result = UsersRepository.singInUser(userSignIn = userSignIn)
        handleSignInResult(result)
    } catch (exception: SerializationException) {
        wrongUserSignInBody()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleSignInResult(userSignInResult: UserSignInResult) {
    when (userSignInResult) {
        is UserSignInResult.UserSignInSuccess -> {
            val updateSessionResult = SessionsRepository.updateSession(userId = userSignInResult.id)
            handleSessionResult(userId = userSignInResult.id, sessionResult = updateSessionResult)
        }
        is UserSignInResult.UserNotSignedUp -> userNotSignedUp()
        is UserSignInResult.WrongPassword -> wrongPassword()
    }
}
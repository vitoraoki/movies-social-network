package com.study.ktor.repository

import com.study.ktor.plugins.route.signin.model.UserSignIn
import com.study.ktor.plugins.route.signin.result.UserSignInResult
import com.study.ktor.plugins.route.signup.result.UserSignUpResult
import com.study.ktor.plugins.route.signup.model.UserSignUp
import com.study.ktor.repository.configuration.DRIVER
import com.study.ktor.repository.configuration.PASSWORD
import com.study.ktor.repository.configuration.URL
import com.study.ktor.repository.configuration.USER_DATABASE
import com.study.ktor.repository.model.User
import com.study.ktor.repository.table.Users
import com.study.ktor.repository.table.Users.toUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UsersRepository {
    init {
        Database.connect(url = URL, driver = DRIVER, user = USER_DATABASE, password = PASSWORD)
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map { it.toUser() }
    }

    fun signUpUser(userSignup: UserSignUp): UserSignUpResult {
        val isUserAlreadySignedUp = isUserAlreadySignedUp(userSignup.email)

        return if (isUserAlreadySignedUp) {
            UserSignUpResult.UserAlreadySignedUp
        } else {
            createUser(userSignup)
        }
    }

    private fun createUser(userSignup: UserSignUp): UserSignUpResult {
        return try {
            val userId = transaction {
                Users.insertAndGetId {
                    it[firstName] = userSignup.firstName
                    it[secondName] = userSignup.secondName
                    it[email] = userSignup.email
                    it[password] = Cryptography.sha512(userSignup.password)
                }.value
            }
            UserSignUpResult.UserSignUpSuccess(id = userId)
        } catch (exception: IllegalStateException) {
            UserSignUpResult.SignUpFailed
        }
    }

    private fun isUserAlreadySignedUp(email: String): Boolean = transaction {
        !Users.select {
            Users.email eq email
        }.limit(1).empty()
    }

    fun singInUser(userSignIn: UserSignIn): UserSignInResult {
        return transaction {
            val userQueryResult = Users.select {
                Users.email eq userSignIn.email
            }

            if (userQueryResult.count() < 1) {
                UserSignInResult.UserNotSignedUp
            } else {
                verifyPassword(userQueryResult = userQueryResult, receivedPassword = userSignIn.password)
            }
        }
    }

    private fun verifyPassword(
        userQueryResult: Query,
        receivedPassword: String
    ): UserSignInResult {
        val user = userQueryResult.first().toUser()
        return if (user.isPasswordCorrect(receivedPassword = receivedPassword)) {
            UserSignInResult.UserSignInSuccess(id = user.id)
        } else {
            UserSignInResult.WrongPassword
        }
    }
}
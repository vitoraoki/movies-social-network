package com.study.ktor.repository

import com.study.ktor.plugins.route.signin.model.UserSignIn
import com.study.ktor.plugins.route.signup.result.UserSignUpResult
import com.study.ktor.plugins.route.signup.model.UserSignUp
import com.study.ktor.repository.configuration.DRIVER
import com.study.ktor.repository.configuration.PASSWORD
import com.study.ktor.repository.configuration.URL
import com.study.ktor.repository.configuration.USER_DATABASE
import com.study.ktor.repository.model.User
import com.study.ktor.repository.table.Users
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
        Users.selectAll().map { Users.toUser(it) }
    }

    fun signUpUser(userSignup: UserSignUp): UserSignUpResult {
        val isUserAlreadySignedUp = isUserAlreadySignedUp(userSignup.email)

        return if (isUserAlreadySignedUp) {
            UserSignUpResult.UserAlreadySignedUp
        } else {
            createUser(userSignup)
        }
    }

    fun singInUser(userSignIn: UserSignIn) {
        val user = Users.select {
            Users.email eq userSignIn.email
        }
        print("")
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
}
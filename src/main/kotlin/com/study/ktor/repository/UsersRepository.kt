package com.study.ktor.repository

import com.study.ktor.plugins.result.UserSignupResult
import com.study.ktor.plugins.route.model.UserSignup
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

    fun signupUser(userSignup: UserSignup): UserSignupResult {
        val isUserAlreadySignedUp = isUserAlreadySignedUp(userSignup.email)

        return if (isUserAlreadySignedUp) {
            UserSignupResult.UserAlreadySignedUp
        } else {
            createUser(userSignup)
        }
    }

    private fun createUser(userSignup: UserSignup): UserSignupResult {
        return try {
            val userId = transaction {
                Users.insertAndGetId {
                    it[firstName] = userSignup.firstName
                    it[secondName] = userSignup.secondName
                    it[email] = userSignup.email
                    it[password] = Cryptography.sha512(userSignup.password)
                }.value
            }
            UserSignupResult.UserSignupSuccess(id = userId)
        } catch (exception: IllegalStateException) {
            UserSignupResult.SignupFailed
        }
    }

    private fun isUserAlreadySignedUp(email: String): Boolean = transaction {
        !Users.select {
            Users.email eq email
        }.limit(1).empty()
    }
}
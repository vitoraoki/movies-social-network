package com.study.ktor.repository

import com.study.ktor.plugins.result.UserSignupResult
import com.study.ktor.repository.configuration.DRIVER
import com.study.ktor.repository.configuration.PASSWORD
import com.study.ktor.repository.configuration.URL
import com.study.ktor.repository.configuration.USER_DATABASE
import com.study.ktor.repository.table.Sessions
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

private const val TOKEN_VALIDITY_IN_DAYS = 5L

object SessionsRepository {
    init {
        Database.connect(url = URL, driver = DRIVER, user = USER_DATABASE, password = PASSWORD)
        transaction {
            SchemaUtils.create(Sessions)
        }
    }

    fun createSession(userId: Int) {
        try {
            transaction {
                Sessions.insert {
                    it[this.userId] = userId
                    it[token] = UUID.randomUUID().toString()
                    it[expiresAt] = LocalDateTime.now(ZoneOffset.UTC)
                        .plusDays(TOKEN_VALIDITY_IN_DAYS)
                        .atZone(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli()
                }
            }
        } catch (exception: IllegalStateException) {
            UserSignupResult.SignupFailed
        }
    }
}
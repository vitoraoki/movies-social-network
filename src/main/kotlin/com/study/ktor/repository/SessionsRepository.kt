package com.study.ktor.repository

import com.study.ktor.plugins.route.session.result.SessionResult
import com.study.ktor.repository.configuration.DRIVER
import com.study.ktor.repository.configuration.PASSWORD
import com.study.ktor.repository.configuration.URL
import com.study.ktor.repository.configuration.USER_DATABASE
import com.study.ktor.repository.table.Sessions
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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

    fun createSession(userId: Int): SessionResult {
        return try {
            val token = generateToken()
            val expiresAt = getExpiresAtDate()

            transaction {
                Sessions.insert {
                    it[this.userId] = userId
                    it[this.token] = token
                    it[this.expiresAt] = expiresAt
                }
            }
            SessionResult.CreateSessionSuccess(token = token, expiresAt = expiresAt)
        } catch (exception: IllegalStateException) {
            SessionResult.CreateSessionFailed
        }
    }

    fun updateSession(userId: Int): SessionResult {
        return try {
            val token = generateToken()
            val expiresAt = getExpiresAtDate()

            transaction {
                Sessions.update({ Sessions.userId eq userId }) {
                    it[this.token] = token
                    it[this.expiresAt] = expiresAt
                }
            }
            SessionResult.UpdateSessionSuccess(token = token, expiresAt = expiresAt)
        } catch (exception: IllegalStateException) {
            SessionResult.UpdateSessionFailed
        }
    }

    private fun generateToken() = UUID.randomUUID().toString()

    private fun getExpiresAtDate() = LocalDateTime.now(ZoneOffset.UTC)
        .plusDays(TOKEN_VALIDITY_IN_DAYS)
        .atZone(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}
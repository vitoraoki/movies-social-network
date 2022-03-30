package com.study.ktor.repository

import com.study.ktor.plugins.route.session.model.SessionValidateData
import com.study.ktor.plugins.route.session.result.SessionResult
import com.study.ktor.repository.configuration.DRIVER
import com.study.ktor.repository.configuration.PASSWORD
import com.study.ktor.repository.configuration.URL
import com.study.ktor.repository.configuration.USER_DATABASE
import com.study.ktor.repository.model.Session
import com.study.ktor.repository.table.Sessions
import com.study.ktor.repository.util.SessionUtil.generateToken
import com.study.ktor.repository.util.SessionUtil.getCurrentDate
import com.study.ktor.repository.util.SessionUtil.getExpiresAtDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SessionsRepository {
    companion object {
        fun connect() {
            Database.connect(url = URL, driver = DRIVER, user = USER_DATABASE, password = PASSWORD)
        }
    }

    init {
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

    fun validateSession(sessionValidateData: SessionValidateData): SessionResult {
        return transaction {
            val sessionQuery = Sessions.select { Sessions.userId eq sessionValidateData.userId }

            if (sessionQuery.count() < 1) {
                SessionResult.SessionNotFound()
            } else {
                handleSessionFound(sessionQuery, sessionValidateData)
            }
        }
    }

    private fun handleSessionFound(
        sessionQuery: Query,
        sessionValidateData: SessionValidateData
    ): SessionResult {
        val session = Sessions.toSession(sessionQuery.first())

        return if (sessionValidateData.token != session.token) {
            SessionResult.InvalidToken()
        } else {
            validateSessionExpireDate(session)
        }
    }

    private fun validateSessionExpireDate(session: Session): SessionResult {
        val currentDate = getCurrentDate()

        return if (currentDate > session.expiresAt) {
            SessionResult.SessionExpired()
        } else {
            SessionResult.ValidSession
        }
    }
}
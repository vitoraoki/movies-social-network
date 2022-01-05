package com.study.ktor.repository.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

private const val TABLE_NAME = "Sessions"
private const val COLUMN_USER_ID = "user_id"
private const val TOKEN = "token"
private const val EXPIRES_AT = "expires_at"

object Sessions: IntIdTable(TABLE_NAME) {
    val userId: Column<Int> = integer(COLUMN_USER_ID)
    val token: Column<String> = text(TOKEN)
    val expiresAt: Column<Long> = long(EXPIRES_AT)
}
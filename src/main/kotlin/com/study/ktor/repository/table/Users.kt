package com.study.ktor.repository.table

import com.study.ktor.repository.model.User
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow

private const val TABLE_NAME = "Users"
private const val COLUMN_FIRST_NAME = "first_name"
private const val COLUMN_SECOND_NAME = "second_name"
private const val COLUMN_EMAIL = "email"
private const val COLUMN_PASSWORD = "password"

object Users: IntIdTable(TABLE_NAME) {
    val firstName: Column<String> = text(COLUMN_FIRST_NAME)
    val secondName: Column<String> = text(COLUMN_SECOND_NAME)
    val email: Column<String> = text(COLUMN_EMAIL)
    val password: Column<String> = text(COLUMN_PASSWORD)

    fun ResultRow.toUser(): User = User(
        id = this[id].value,
        firstName = this[firstName],
        secondName = this[secondName],
        email = this[email],
        password = this[password]
    )
}
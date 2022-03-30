package com.study.ktor.plugins

import com.study.ktor.repository.SessionsRepository
import com.study.ktor.repository.UsersRepository

fun connectDatabases() {
    UsersRepository.connect()
    SessionsRepository.connect()
}
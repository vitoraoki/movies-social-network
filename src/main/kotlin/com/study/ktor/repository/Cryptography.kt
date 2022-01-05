package com.study.ktor.repository

import java.security.MessageDigest

private const val SHA_256 = "SHA-256"
private const val SHA_512 = "SHA-512"

object Cryptography {
    fun sha256(input: String) = hashInput(algorithm = SHA_256, input = input)
    fun sha512(input: String) = hashInput(algorithm = SHA_512, input = input)

    private fun hashInput(algorithm: String, input: String): String = MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .toHexString()

    private fun ByteArray.toHexString(): String {
        return this.joinToString("") {
            String.format("%02X", it)
        }
    }
}
package org.acme.config

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.inject.Singleton

@Singleton
class PasswordHelper {
    private val encoder = BCryptPasswordEncoder()

    fun encodePwd(input: String): String {
        return encoder.encode(input)
    }

    fun isPasswordValid(input: String, hashPwd: String): Boolean {
        return encoder.matches(input, hashPwd)
    }
}
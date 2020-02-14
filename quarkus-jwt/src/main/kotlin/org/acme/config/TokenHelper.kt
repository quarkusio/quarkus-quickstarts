package org.acme.config

import io.smallrye.jwt.build.Jwt
import org.acme.data.UsersService
import org.acme.models.User
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.inject.Singleton

@Singleton
class TokenHelper(private val usersSvc: UsersService) {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    private var issuer: String = ""

    private fun setClaims(user: User): Map<String, Any> {
        val roles = usersSvc.getUserGroups(user.username)
        return mapOf("email" to user.email, "groups" to roles)
    }

    fun getToken(user: User): String {
        return generateToken(user)
    }

    private fun generateToken(user: User): String {
        return Jwt.claims(setClaims(user)).issuer(issuer).sign()
    }
}
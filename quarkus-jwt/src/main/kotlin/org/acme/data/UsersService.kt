package org.acme.data

import org.acme.config.PasswordHelper
import org.acme.models.Authorities
import org.acme.models.User
import javax.inject.Singleton

@Singleton
class UsersService(private val securityHelper: PasswordHelper, private val userRepo: UsersRepository) {

    private fun getUserByUsername(username: String): User? {
        return try {
            userRepo.getUserByUsername(username)
        } catch (x: Exception) {
            // log error x.message!!
            null
        }
    }

    fun save(user: User): Long? {
        return try {
            user.password = securityHelper.encodePwd(user.password)
            userRepo.save(user).id
        } catch (x: Exception) {
            // log error x.message!!
            null
        }
    }

    private fun isUserValid(username: String, password: String): Boolean {
        val user = getUserByUsername(username)
        return (user != null && securityHelper.isPasswordValid(password, user.password))
    }

    fun authenticate(username: String, password: String): Boolean {
        try {
            val user = getUserByUsername(username)
            return (user != null && isUserValid(username, password))

        } catch (x: Exception) {
            // log error x.message!!
        }
        return false
    }

    fun isUsernameTaken(username: String): Boolean {
        return getUserByUsername(username) != null
    }

    private fun getUserRoles(username: String): List<Authorities> {
        return userRepo.getRolesByUsername(username)
    }
    fun getUserGroups(username: String) = getUserRoles(username).map { it.authority }
}
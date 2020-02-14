package org.acme.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.acme.models.Authorities
import org.acme.models.User

interface UsersRepository : JpaRepository<User, Long> {
    fun getUserByUsername(username: String): User?

    @Query("select a from Authorities a where a.username=:username")
    fun getRolesByUsername(@Param("username") username: String): List<Authorities>
}
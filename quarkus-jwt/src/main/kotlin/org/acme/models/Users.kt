package org.acme.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
data class User (
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,
        @get:NotBlank
        var username: String = "",
        @get:NotBlank(message = "Email is mandatory")
        @get:Email(message = "Must be a valid email address")
        var email: String = "",
        @get:NotBlank(message = "Password is mandatory")
        var password: String = ""
)
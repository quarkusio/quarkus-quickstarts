package org.acme.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Authorities(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,
        var username: String = "",
        var authority: String = ""
)
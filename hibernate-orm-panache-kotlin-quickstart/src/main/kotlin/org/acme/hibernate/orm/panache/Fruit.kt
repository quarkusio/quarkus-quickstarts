package org.acme.hibernate.orm.panache

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Cacheable
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
@Cacheable
class Fruit : PanacheEntity {
    companion object: PanacheCompanion<Fruit>
    @Column(length = 40, unique = true)
    lateinit var name: String

    constructor()

    constructor(name: String) {
        this.name = name
    }
}
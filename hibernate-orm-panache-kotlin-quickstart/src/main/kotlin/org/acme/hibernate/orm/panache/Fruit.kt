package org.acme.hibernate.orm.panache

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Cacheable
import javax.persistence.Column
import javax.persistence.Entity

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
package org.acme.hibernate.orm.panache.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Cacheable
public class FruitEntity extends PanacheEntity {

    @Column(length = 40, unique = true)
    public String name;

    public FruitEntity() {
    }

    public FruitEntity(String name) {
        this.name = name;
    }
}

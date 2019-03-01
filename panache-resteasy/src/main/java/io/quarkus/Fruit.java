package io.quarkus;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.panache.jpa.PanacheEntity;

@Entity
@Cacheable
public class Fruit extends PanacheEntity {

    @Column(length = 40, unique = true)
    public String name;

    public Fruit() {
    }

    public Fruit(String name) {
        this.name = name;
    }
}

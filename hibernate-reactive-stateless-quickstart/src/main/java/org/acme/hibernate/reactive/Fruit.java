package org.acme.hibernate.reactive;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
public class Fruit {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 40, unique = true)
    private String name;

    Fruit() {}

    public Fruit(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fruit{" + id + "," + name + '}';
    }
}

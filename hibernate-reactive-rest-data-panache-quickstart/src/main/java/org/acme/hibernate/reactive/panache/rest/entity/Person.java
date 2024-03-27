package org.acme.hibernate.reactive.panache.rest.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

@Entity(name = "PersonForEntity")
@NamedQuery(name = "PersonForEntity.containsInName", query = "from PersonForEntity where name like CONCAT('%', CONCAT(:name, '%'))")
public class Person extends PanacheEntity {
    public String name;
    public LocalDate birthDate;
}

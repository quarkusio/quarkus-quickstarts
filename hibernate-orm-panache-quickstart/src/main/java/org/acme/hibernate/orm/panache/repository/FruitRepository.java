package org.acme.hibernate.orm.panache.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FruitRepository implements PanacheRepository<Fruit> {
}

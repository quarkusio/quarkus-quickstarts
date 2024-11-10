package org.acme.hibernate.orm.panache.rest.repository;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {
}

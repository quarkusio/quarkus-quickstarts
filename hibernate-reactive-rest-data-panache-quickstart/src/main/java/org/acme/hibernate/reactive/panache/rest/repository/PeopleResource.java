package org.acme.hibernate.reactive.panache.rest.repository;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheRepositoryResource;

public interface PeopleResource extends PanacheRepositoryResource<PersonRepository, Person, Long> {

}

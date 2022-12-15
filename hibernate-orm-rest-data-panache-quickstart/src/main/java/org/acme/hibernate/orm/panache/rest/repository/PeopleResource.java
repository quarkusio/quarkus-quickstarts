package org.acme.hibernate.orm.panache.rest.repository;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;

public interface PeopleResource extends PanacheRepositoryResource<PersonRepository, Person, Long> {

}

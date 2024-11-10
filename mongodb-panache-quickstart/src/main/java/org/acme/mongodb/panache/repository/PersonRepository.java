package org.acme.mongodb.panache.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PersonRepository implements PanacheMongoRepository<Person> {
    public Person findByName(String name) {
        return find("name", name).firstResult();
    }

    public List<Person> findAlive() {
        return list("status", Status.LIVING);
    }

    public void deleteLoics() {
        delete("name", "Loïc");
    }
}

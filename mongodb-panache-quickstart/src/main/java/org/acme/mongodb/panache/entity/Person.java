package org.acme.mongodb.panache.entity;

import java.time.LocalDate;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

@MongoEntity(collection = "ThePerson")
public class Person extends PanacheMongoEntity {
    public String name;

    // will be persisted as a 'birth' field in MongoDB
    @BsonProperty("birth")
    public LocalDate birthDate;

    public Status status;

    // return name as lowercase in the model
    public String getName() {
        return name.toLowerCase();
    }

    // store all names in uppercase in the DB
    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    // entity methods
    public static Person findByName(String name) {
        return find("name", name).firstResult();
    }

    public static List<Person> findAlive() {
        return list("status", Status.LIVING);
    }

    public static void deleteLoics() {
        delete("name", "Loïc");
    }
}

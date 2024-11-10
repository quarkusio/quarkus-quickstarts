package org.acme.mongodb.panache.repository;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@MongoEntity(collection = "ThePerson")
public class Person {
    public ObjectId id;

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

}
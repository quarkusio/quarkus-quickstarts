package org.acme.mongodb;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class ReactiveFruitService {

    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<List<Fruit>> list() {
        return getCollection().find()
                .map(doc -> {
                    Fruit fruit = new Fruit();
                    fruit.setName(doc.getString("name"));
                    fruit.setDescription(doc.getString("description"));
                    return fruit;
                }).collect().asList();
    }

    public Uni<Void> add(Fruit fruit) {
        Document document = new Document()
                .append("name", fruit.getName())
                .append("description", fruit.getDescription());
        return getCollection().insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }

    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("fruit").getCollection("reactive_fruit");
    }
}

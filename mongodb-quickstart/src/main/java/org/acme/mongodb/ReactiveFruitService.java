package org.acme.mongodb;

import java.util.List;
import java.util.UUID;

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
                    fruit.setId(doc.getString("id"));
                    fruit.setDescription(doc.getString("description"));
                    return fruit;
                }).collect().asList();
    }

    public Uni<Void> add(Fruit fruit) {

        /*
            This is usually going to be done in the data layer itself (See FruitCodec for a better example)
         */
        fruit.setId(UUID.randomUUID().toString());

        Document document = new Document()
                .append("name", fruit.getName())
                .append("id", fruit.getId())
                .append("description", fruit.getDescription());
        return getCollection().insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }

    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("fruit").getCollection("fruit");
    }
}

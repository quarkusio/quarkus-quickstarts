package org.acme.rest.json;

import io.quarkus.mongodb.ReactiveMongoClient;
import io.quarkus.mongodb.ReactiveMongoCollection;
import org.bson.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ReactiveFruitService {

    @Inject
    ReactiveMongoClient mongoClient;

    public CompletionStage<List<Fruit>> list(){
        return getCollection().find().map(doc -> {
            Fruit fruit = new Fruit();
            fruit.setName(doc.getString("name"));
            fruit.setDescription(doc.getString("description"));
            return fruit;
        }).toList().run();
    }

    public CompletionStage<Void> add(Fruit fruit){
        Document document = new Document()
                .append("name", fruit.getName())
                .append("description", fruit.getDescription());
        return getCollection().insertOne(document);
    }

    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("fruit").getCollection("fruit");
    }
}
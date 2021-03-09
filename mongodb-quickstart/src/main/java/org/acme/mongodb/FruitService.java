package org.acme.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@ApplicationScoped
public class FruitService {

    @Inject
    MongoClient mongoClient;

    public List<Fruit> list() {
        List<Fruit> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Fruit fruit = new Fruit();
                fruit.setName(document.getString("name"));
                fruit.setDescription(document.getString("description"));
                fruit.setId(document.getString("id"));
                list.add(fruit);
            }
        }
        return list;
    }

    public void add(Fruit fruit) {

        /*
            This is usually going to be done in the data layer itself (See FruitCodec for a better example)
         */
        fruit.setId(UUID.randomUUID().toString());

        Document document = new Document()
                .append("name", fruit.getName())
                .append("id", fruit.getId())
                .append("description", fruit.getDescription());
        getCollection().insertOne(document);
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("fruit").getCollection("fruit");
    }
}

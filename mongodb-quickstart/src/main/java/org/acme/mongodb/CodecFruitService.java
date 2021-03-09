package org.acme.mongodb;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@ApplicationScoped
public class CodecFruitService {

    @Inject
    MongoClient mongoClient;

    public List<Fruit> list() {
        List<Fruit> list = new ArrayList<>();

        try (MongoCursor<Fruit> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        return list;
    }

    public void add(Fruit fruit) {
        getCollection().insertOne(fruit);
    }

    private MongoCollection<Fruit> getCollection() {
        return mongoClient.getDatabase("fruit").getCollection("fruit", Fruit.class);
    }
}

package org.acme.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.bson.Document;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

public class MongoResource implements QuarkusTestResourceLifecycleManager {

    private static final String MONGODB_VERSION = "mongo:4.4.4";
    private final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse(MONGODB_VERSION));
    private MongoClient client;

    @Override
    public Map<String, String> start() {
        mongo.start();
        client = MongoClients.create(endpoint());

        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.mongodb.connection-string", endpoint());
        return properties;
    }

    @Override
    public void stop() {
        if (mongo.isRunning()) {
            mongo.stop();
        }
    }

    private String endpoint() {
        return String.format("mongodb://%s:%s",mongo.getHost(), mongo.getFirstMappedPort());
    }

}

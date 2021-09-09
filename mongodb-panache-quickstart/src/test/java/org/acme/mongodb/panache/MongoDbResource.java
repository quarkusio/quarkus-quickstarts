package org.acme.mongodb.panache;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class MongoDbResource implements QuarkusTestResourceLifecycleManager {
    public static final String MONGO_IMAGE_NAME = "mongo:4.2.6";
    public static final int MONGO_PORT = 27017;

    private GenericContainer<?> mongoContainer;

    @Override
    public Map<String, String> start() {
        mongoContainer = new GenericContainer<>(MONGO_IMAGE_NAME).withExposedPorts(MONGO_PORT);
        mongoContainer.waitingFor(new HostPortWaitStrategy()).start();
        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.mongodb.connection-string",
                String.format("mongodb://%s:%d", mongoContainer.getHost(), mongoContainer.getMappedPort(MONGO_PORT)));
        return properties;
    }

    @Override
    public void stop() {
        mongoContainer.stop();
    }
}

package org.acme.mongodb.panache;

import org.testcontainers.containers.GenericContainer;

import javax.validation.constraints.NotNull;

public class MongoDbContainer extends GenericContainer<MongoDbContainer> {

    public static final String DEFAULT_IMAGE_AND_TAG = "mongo:4.2.6";
    public static final String MONGODB_HOST = "localhost";
    public static final int MONGODB_PORT = 27017;

    public MongoDbContainer() {
        this(DEFAULT_IMAGE_AND_TAG);
    }

    public MongoDbContainer(@NotNull String image) {
        super(image);
        addExposedPort(MONGODB_PORT);
    }

    @NotNull
    public Integer getPort() {
        return getMappedPort(MONGODB_PORT);
    }
}
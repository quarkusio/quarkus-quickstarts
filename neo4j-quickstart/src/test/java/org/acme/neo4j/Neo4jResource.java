package org.acme.neo4j;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.Neo4jContainer;

public class Neo4jResource implements QuarkusTestResourceLifecycleManager {

    @SuppressWarnings("rawtypes")
    private static final Neo4jContainer NEO4J = new Neo4jContainer<>("neo4j:4.0.0")
            .withAdminPassword(null);

    @Override
    public Map<String, String> start() {
        NEO4J.start();
        return Collections.singletonMap("quarkus.neo4j.uri", NEO4J.getBoltUrl());
    }

    @Override
    public void stop() {
        NEO4J.close();
    }
}

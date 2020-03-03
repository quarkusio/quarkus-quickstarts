package org.acme.neo4j;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.Neo4jContainer;

public class Neo4jResource implements QuarkusTestResourceLifecycleManager {

    final Neo4jContainer NEO4J = new Neo4jContainer<>("neo4j:4.0.0").withAdminPassword(null);

    @Override
    public Map<String, String> start() {
        NEO4J.start();
        System.setProperty("quarkus.neo4j.uri", NEO4J.getBoltUrl());
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        System.clearProperty("quarkus.neo4j.uri");
        NEO4J.close();
    }
}

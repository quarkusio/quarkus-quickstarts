package org.acme.neo4j;

import org.neo4j.driver.types.Node;

public class Fruit {

    public Long id;

    public String name;

    public static Fruit from(Node node) {
        return new Fruit(node.id(), node.get("name").asString());
    }

    public Fruit() {
        // This is neaded for the REST-Easy JSON Binding
    }

    public Fruit(String name) {
        this.name = name;
    }

    public Fruit(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
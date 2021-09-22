package org.acme;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Fruit extends PanacheMongoEntity {
    public String name;
    public String color;
}

package org.acme.dynamodb;

import java.util.Map;
import java.util.Objects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RegisterForReflection
public class Fruit {

    private String name;
    private String description;

    public Fruit() {
    }

    public static Fruit from(Map<String, AttributeValue> item) {
        Fruit fruit = new Fruit();
        if (item != null && !item.isEmpty()) {
            fruit.setName(item.get(AbstractService.FRUIT_NAME_COL).s());
            fruit.setDescription(item.get(AbstractService.FRUIT_DESC_COL).s());
        }
        return fruit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Fruit)) {
            return false;
        }

        Fruit other = (Fruit) obj;

        return Objects.equals(other.name, this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
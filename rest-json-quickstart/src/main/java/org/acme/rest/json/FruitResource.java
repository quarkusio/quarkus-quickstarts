package org.acme.rest.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Path("/fruits")
public class FruitResource {

    private final Set<Fruit> fruits = new CopyOnWriteArraySet<>();

    public FruitResource(ObjectMapper mapper) {
        // To demonstrate how you can load initial data from a JSON file on the classpath, we read fruits.json
        // and populate the set. The file is located in src/main/resources.
        try (InputStream is = getClass().getResourceAsStream("/fruits.json")) {
            if (is == null) {
                throw new IllegalStateException("fruits.json not found on classpath");
            }
            List<Fruit> loadedFruits = mapper.readValue(is, new TypeReference<List<Fruit>>() {
            });
            fruits.addAll(loadedFruits);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load fruits.json", e);
        }
    }

    @GET
    public Set<Fruit> list() {
        return fruits;
    }

    @POST
    public Set<Fruit> add(Fruit fruit) {
        fruits.add(fruit);
        return fruits;
    }

    @DELETE
    public Set<Fruit> delete(Fruit fruit) {
        fruits.removeIf(existingFruit -> existingFruit.name.contentEquals(fruit.name));
        return fruits;
    }
}

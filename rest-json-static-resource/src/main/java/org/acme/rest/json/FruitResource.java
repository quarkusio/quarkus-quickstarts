package org.acme.rest.json;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class    FruitResource {

    private Set<Fruit> fruits;

    public FruitResource() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        try(Jsonb jsonb = JsonbBuilder.create()){
            fruits = jsonb.fromJson(classLoader.getResourceAsStream("fruits.json"), HashSet.class);
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
        fruits.remove(fruit);
        return fruits;
    }
}

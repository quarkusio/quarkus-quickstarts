package org.acme;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.bson.types.ObjectId;

@Path("/fruits")
public class FruitResource {

    @GET
    public List<Fruit> list() {
        return Fruit.listAll();
    }

    @GET
    @Path("/{id}")
    public Fruit get(String id) {
        return Fruit.findById(new ObjectId(id));
    }

    @POST
    public void save(Fruit fruit) {
        fruit.persist();
    }
}

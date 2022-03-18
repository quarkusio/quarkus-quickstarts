package org.acme;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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

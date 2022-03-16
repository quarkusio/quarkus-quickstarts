package org.acme;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
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

package org.acme;

import org.bson.types.ObjectId;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
public class FruitResource {

    @GET
    public List<Fruit> list() {
        return Fruit.listAll();
    }

    @GET
    @Path("/{id}")
    public Fruit get(@PathParam("id") String id) {
        return Fruit.findById(new ObjectId(id));
    }

    @POST
    public void save(Fruit fruit) {
        fruit.persist();
    }
}

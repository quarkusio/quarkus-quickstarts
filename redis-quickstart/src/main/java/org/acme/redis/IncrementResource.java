package org.acme.redis;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;

@Path("/increments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IncrementResource {

    @Inject
    IncrementService service;

    @GET
    public Uni<List<String>> keys() {
        return service.keys();
    }

    @POST
    public Increment create(Increment increment) {
        service.set(increment.key, increment.value);
        return increment;
    }

    @GET
    @Path("/{key}")
    public Increment get(String key) {
        return new Increment(key, Integer.valueOf(service.get(key)));
    }

    @PUT
    @Path("/{key}")
    public void increment(String key, Integer value) {
        service.increment(key, value);
    }

    @DELETE
    @Path("/{key}")
    public Uni<Void> delete(String key) {
        return service.del(key);
    }
}

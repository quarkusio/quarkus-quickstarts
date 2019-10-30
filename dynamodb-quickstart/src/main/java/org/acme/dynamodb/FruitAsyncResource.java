package org.acme.dynamodb;

import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/async-fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitAsyncResource {

    @Inject
    FruitAsyncService service;

    @GET
    public CompletionStage<List<Fruit>> getAll() {
        return service.findAll();
    }

    @GET
    @Path("{name}")
    public CompletionStage<Fruit> getSingle(@PathParam("name") String name) {
        return service.get(name);
    }

    @POST
    public CompletionStage<List<Fruit>> add(Fruit fruit) {
        service.add(fruit);
        return getAll();
    }
}
package org.acme.dynamodb;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import io.smallrye.mutiny.Uni;

@Path("/async-fruits")
public class FruitAsyncResource {

    @Inject
    FruitAsyncService service;

    @GET
    public Uni<List<Fruit>> getAll() {
        return service.findAll();
    }

    @GET
    @Path("{name}")
    public Uni<Fruit> getSingle(String name) {
        return service.get(name);
    }

    @POST
    public Uni<List<Fruit>> add(Fruit fruit) {
        return service.add(fruit)
                .onItem().ignore().andSwitchTo(this::getAll);
    }
}

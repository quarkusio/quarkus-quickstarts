package org.acme.mongodb;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import io.smallrye.mutiny.Uni;

@Path("/reactive_fruits")
public class ReactiveFruitResource {

    @Inject
    ReactiveFruitService fruitService;

    @GET
    public Uni<List<Fruit>> list() {
        return fruitService.list();
    }

    @POST
    public Uni<List<Fruit>> add(Fruit fruit) {
        return fruitService.add(fruit)
                .onItem().ignore().andSwitchTo(this::list);
    }
}

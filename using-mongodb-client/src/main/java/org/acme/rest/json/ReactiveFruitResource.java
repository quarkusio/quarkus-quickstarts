package org.acme.rest.json;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/reactive_fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReactiveFruitResource {

    @Inject ReactiveFruitService fruitService;


    @GET
    public CompletionStage<List<Fruit>> list() {
        return fruitService.list();
    }

    @POST
    public CompletionStage<List<Fruit>>  add(Fruit fruit) {
        return fruitService.add(fruit).thenCompose(x -> list());
    }
}

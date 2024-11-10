package org.acme.mongodb;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/codec_fruits")
public class CodecFruitResource {

    @Inject
    CodecFruitService fruitService;

    @GET
    public List<Fruit> list() {
        return fruitService.list();
    }

    @POST
    public List<Fruit> add(Fruit fruit) {
        fruitService.add(fruit);
        return list();
    }
}
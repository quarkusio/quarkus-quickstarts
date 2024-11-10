package org.acme.panache;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import io.smallrye.mutiny.Uni;

@Path("/prices")
public class PriceResource {

    @GET
    public Uni<List<Price>> getAllPrices() {
        return Price.listAll();
    }
}

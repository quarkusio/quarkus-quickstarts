package org.acme.panache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/prices")
public class PriceResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Price> getAllPrices() {
        return Price.listAll();
    }
}

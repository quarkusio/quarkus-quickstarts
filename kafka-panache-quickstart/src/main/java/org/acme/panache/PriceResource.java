package org.acme.panache;

import io.smallrye.common.annotation.Blocking;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/prices")
public class PriceResource {

    /**
     * We uses classic Hibernate, so the API is blocking, so we need to use @Blocking.
     * @return the list of prices
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking
    public List<Price> getAllPrices() {
        return Price.listAll();
    }
}

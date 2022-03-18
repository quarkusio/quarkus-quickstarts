package org.acme.panache;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.smallrye.common.annotation.Blocking;

@Path("/prices")
public class PriceResource {

    /**
     * We uses classic Hibernate, so the API is blocking, so we need to use @Blocking.
     * @return the list of prices
     */
    @GET
    @Blocking
    public List<Price> getAllPrices() {
        return Price.listAll();
    }
}

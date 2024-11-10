package org.acme.jms;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * A simple resource showing the last price.
 */
@Path("/prices")
public class PriceResource {

    @Inject
    PriceConsumer consumer;

    @GET
    @Path("last")
    @Produces(MediaType.TEXT_PLAIN)
    public String last() {
        return consumer.getLastPrice();
    }
}

package org.acme.kafka;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Publisher;

import org.eclipse.microprofile.reactive.messaging.Channel;

/**
 * A simple resource retrieving the "in-memory" "my-data-stream" and sending the items to a server sent event.
 */
@Path("/")
public class PriceResource {

    @Inject
    @Channel("my-data-stream")
    Publisher<Price> prices;

    @GET
    @Path("/prices")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    public Publisher<Price> stream() {
        return prices;
    }
}

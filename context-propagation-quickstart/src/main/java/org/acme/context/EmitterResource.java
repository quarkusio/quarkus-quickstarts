package org.acme.context;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import io.smallrye.mutiny.Multi;

@Path("/")
public class EmitterResource {

    // Get the prices stream
    @Inject
    @Channel("prices") Publisher<Double> prices;

    @Transactional
    @GET
    @Path("/prices")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.TEXT_PLAIN)
    public Publisher<Double> prices() {
        // get the next three prices from the price stream
        return Multi.createFrom().publisher(prices)
                .transform().byTakingFirstItems(3)
                .map(price -> {
                    // store each price before we send them
                    Price priceEntity = new Price();
                    priceEntity.value = price;
                    // here we are all in the same transaction
                    // thanks to context propagation
                    priceEntity.persist();
                    return price;
                    // the transaction is committed once the stream completes
                });
    }

    @GET
    @Path("/prices/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Price> getAllPrices() {
        return Price.listAll();
    }
}

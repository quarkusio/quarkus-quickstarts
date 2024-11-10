package org.acme.context;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.reactive.RestStreamElementType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;

@Path("/")
public class EmitterResource {

    // Get the prices stream
    @Inject
    @Channel("prices") Multi<Double> prices;

    @Transactional
    @GET
    @Path("/prices")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public Multi<Double> prices() {
        // get the next three prices from the price stream
        return Multi.createFrom().publisher(prices)
                .select().first(3)
                // The items are received from the event loop, so cannot use Hibernate ORM (classic)
                // Switch to a worker thread, the transaction will be propagated
                .emitOn(Infrastructure.getDefaultExecutor())
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
    public List<Price> getAllPrices() {
        return Price.listAll();
    }
}

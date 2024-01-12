package org.acme.redis.streams.aggregator;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.redis.streams.aggregator.processors.TemperatureProcessor;

@Path("/temperatures")
public class AggregatorResource {

    @Inject
    TemperatureProcessor processor;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Uni<String> getTemperatureAggregatesPerStationId(String id) {

        return processor.getTemperatureAggregateByStationId(id);
    }
}

package org.acme.extra;

import java.time.Duration;
import java.util.Date;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.smallrye.mutiny.Multi;

@Path("/hello")
public class StreamingResource {

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("{name}/streaming")
    public Multi<String> greeting(String name) {
        return Multi.createFrom().ticks().every(Duration.ofMillis(2000))
                .map(l -> String.format("Hello %s! (%s)%n", name, new Date()));
    }
}

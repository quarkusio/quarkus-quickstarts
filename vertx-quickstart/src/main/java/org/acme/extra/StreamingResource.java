package org.acme.extra;

import java.util.Date;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.Vertx;

@Path("/hello")
public class StreamingResource {

    @Inject
    Vertx vertx;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("{name}/streaming")
    public Multi<String> greeting(String name) {
        return vertx.periodicStream(2000).toMulti().map(l -> String.format("Hello %s! (%s)%n", name, new Date()));
    }
}

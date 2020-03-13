package org.acme.vertx;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.smallrye.mutiny.Multi;
import io.vertx.axle.core.Vertx;

@Path("/hello")
public class StreamingResource {

    @Inject
    Vertx vertx;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("{name}/streaming")
    public Multi<String> greeting(@PathParam String name) {
        return Multi.createFrom().publisher(vertx.periodicStream(2000).toPublisher())
                .map(l -> String.format("Hello %s! (%s)%n", name, new Date()));
    }
}

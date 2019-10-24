package org.acme.vertx;

import io.vertx.axle.core.Vertx;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/hello")
public class StreamingResource {

    @Inject
    Vertx vertx;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("{name}/streaming")
    public Publisher<String> greeting(@PathParam String name) {
        return ReactiveStreams.fromPublisher(vertx.periodicStream(2000).toPublisher())
                .map(l -> String.format("Hello %s! (%s)%n", name, new Date()))
                .buildRs();
    }
}

package org.acme.vertx;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;

@Path("/hello")
public class GreetingResource {

    @Inject
    Vertx vertx;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{name}")
    public Uni<String> greeting(@PathParam String name) {
        return Uni.createFrom().emitter(emitter -> {
            long start = System.nanoTime();
            // Delay reply by 10ms
            vertx.setTimer(10, l -> {
                // Compute elapsed time in milliseconds
                long duration = MILLISECONDS.convert(System.nanoTime() - start, NANOSECONDS);
                String message = String.format("Hello %s! (%d ms)%n", name, duration);
                emitter.complete(message);
            });
        });
    }
}

package org.acme.scheduler;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/count")
public class CountResource {

    @Inject
    CounterBean counter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "count: " + counter.get();
    }
}

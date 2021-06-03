package org.acme.opentelemetry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public interface ResourceClient {
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    String hello();
}

package org.acme.resources;

import org.acme.services.GreetingService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/hello")
public class GreetingResource {

    @Inject
    GreetingService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{name}")
    public String greeting(@PathParam String name) {
        return service.greeting(name).getMessage();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return service.greeting().getMessage();
    }
}

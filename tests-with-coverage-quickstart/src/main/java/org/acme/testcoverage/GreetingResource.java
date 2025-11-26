package org.acme.testcoverage;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @ConfigProperty(name = "hi.message", defaultValue = "Hi")
    String message;

    private final GreetingService service;

    @Inject
    public GreetingResource(GreetingService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting/{name}")
    public String greeting(String name) {
        return service.greeting(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    // This method is only tested in a QuarkusComponentTest
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hi/{name}")
    public String hi(String name) {
        return message + " " + name + "!";
    }
}

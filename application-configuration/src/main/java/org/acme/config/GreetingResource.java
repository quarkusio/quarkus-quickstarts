package org.acme.config;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/greeting")
public class GreetingResource {

    @Inject
    @ConfigProperty(name = "greeting.message")
    private String message;

    @Inject
    @ConfigProperty(name = "greeting.suffix", defaultValue="!")
    private String suffix;

    @Inject
    @ConfigProperty(name = "greeting.name")
    private Optional<String> name;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return message + " " + name.orElse("world") + suffix;
    }
}

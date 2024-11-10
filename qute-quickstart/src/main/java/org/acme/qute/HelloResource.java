package org.acme.qute;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/hello")
public class HelloResource {

    @Inject
    Template hello;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@RestQuery String name) {
        return hello.data("name", name);
    }

}

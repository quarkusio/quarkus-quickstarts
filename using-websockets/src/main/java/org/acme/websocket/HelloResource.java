package org.acme.websocket;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/assets/{path: .*}")
    public Response staticResources(@PathParam("path") final String path) {
        InputStream resource = HelloResource.class.getClassLoader().getResourceAsStream("/assets/" + path);
        return resource == null ? Response.status(404).build() : Response.ok().entity(resource).build();
    }
}

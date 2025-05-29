package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/poems")
public class Poems {

    @Inject
    AiPoemService aiPoemService;  // <1>

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{topic}/{lines}")  // <2>
    public String showMeAPoem(@PathParam("topic") String userTopic, @PathParam("lines") int userLines) {  // <3>
        return aiPoemService.writeAPoem(userTopic, userLines);  // <4>
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }
}

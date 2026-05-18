package org.acme.rest.json;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/legumes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LegumeResource {

    private Set<Legume> legumes = Collections.synchronizedSet(new LinkedHashSet<>());

    public LegumeResource() {
        legumes.add(new Legume("Carrot", "Root vegetable, usually orange"));
        legumes.add(new Legume("Zucchini", "Summer squash"));
    }

    @GET
    public Response list() {
        return Response.ok(legumes).build();
    }
}

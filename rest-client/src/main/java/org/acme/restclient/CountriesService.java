package org.acme.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Set;

@Path("/v2")
@RegisterRestClient
public interface CountriesService {

    @GET
    @Path("/name/{name}")
    @Produces("application/json")
    Set<Country> getByName(@PathParam("name") String name);
}

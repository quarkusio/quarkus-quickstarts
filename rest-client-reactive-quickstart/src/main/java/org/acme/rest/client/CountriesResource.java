package org.acme.rest.client;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Set;
import java.util.concurrent.CompletionStage;

@Path("/country")
public class CountriesResource {

    @RestClient
    CountriesService countriesService;


    @GET
    @Path("/name/{name}")
    @Blocking
    public Set<Country> name(@PathParam("name") String name) {
        return countriesService.getByName(name);
    }

    @GET
    @Path("/name-async/{name}")
    public CompletionStage<Set<Country>> nameAsync(@PathParam("name") String name) {
        return countriesService.getByNameAsync(name);
    }

    @GET
    @Path("/name-uni/{name}")
    public Uni<Set<Country>> nameUni(@PathParam("name") String name) {
        return countriesService.getByNameAsUni(name);
    }
}
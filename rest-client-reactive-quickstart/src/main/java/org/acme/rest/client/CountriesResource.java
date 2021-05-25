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
    @Path("/name/{countryName}")
    @Blocking
    public Set<Country> name(String countryName) {
        return countriesService.getByName(countryName);
    }

    @GET
    @Path("/name-async/{countryName}")
    public CompletionStage<Set<Country>> nameAsync(String countryName) {
        return countriesService.getByNameAsync(countryName);
    }

    @GET
    @Path("/name-uni/{countryName}")
    public Uni<Set<Country>> nameUni(String countryName) {
        return countriesService.getByNameAsUni(countryName);
    }
}

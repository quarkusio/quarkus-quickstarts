package org.acme.rest.client;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Set;
import java.util.concurrent.CompletionStage;

@Path("/v2")
@RegisterRestClient(configKey="country-api")
public interface CountriesService {

    @GET
    @Path("/name/{countryName}")
    Set<Country> getByName(String countryName);


    @GET
    @Path("/name/{countryName}")
    CompletionStage<Set<Country>> getByNameAsync(String countryName);

    @GET
    @Path("/name/{countryName}")
    Uni<Set<Country>> getByNameAsUni(String countryName);
}

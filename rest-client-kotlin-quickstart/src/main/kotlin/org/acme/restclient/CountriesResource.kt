package org.acme.restclient

import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.resteasy.annotations.jaxrs.PathParam
import java.util.concurrent.CompletionStage
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/country")
class CountriesResource {

    @Inject
    @field: RestClient
    lateinit var countriesService: CountriesService

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    fun name(@PathParam(value = "name") name: String): Set<Country> {
        return countriesService.getByName(name)
    }

    @GET
    @Path("/name-async/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    fun nameAsync(@PathParam(value = "name") name: String): CompletionStage<Set<Country>> {
        return countriesService.getByNameAsync(name)
    }
}
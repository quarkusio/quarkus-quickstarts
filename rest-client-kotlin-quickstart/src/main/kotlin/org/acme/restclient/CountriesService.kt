package org.acme.restclient

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.jboss.resteasy.annotations.jaxrs.PathParam
import java.util.concurrent.CompletionStage
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/v2")
@RegisterRestClient
interface CountriesService {

    @GET
    @Path("/name/{name}")
    @Produces("application/json")
    fun getByName(@PathParam(value = "name") name: String): Set<Country>

    @GET
    @Path("/name/{name}")
    @Produces("application/json")
    fun getByNameAsync(@PathParam(value = "name") name: String): CompletionStage<Set<Country>>
}

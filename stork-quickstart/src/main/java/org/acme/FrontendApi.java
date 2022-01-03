package org.acme;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A frontend API using our REST Client (which uses Stork to locate and select the service instance on each call).
 */
@Path("/api")
public class FrontendApi {

    @RestClient MyService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String invoke() {
        return service.get();
    }

}

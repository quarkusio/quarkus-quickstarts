package org.acme.security.openid.connect.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@RegisterRestClient
@Path("/")
public interface RestClientWithTokenHeaderParam {

    @GET
    @Produces("text/plain")
    @Path("userName")
    Uni<String> getUserName(@HeaderParam("Authorization") String authorization);
    
    @GET
    @Produces("text/plain")
    @Path("adminName")
    Uni<String> getAdminName(@HeaderParam("Authorization") String authorization);
}

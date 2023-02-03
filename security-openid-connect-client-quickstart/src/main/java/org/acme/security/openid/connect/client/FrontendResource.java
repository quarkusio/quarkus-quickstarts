package org.acme.security.openid.connect.client;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;

@Path("/frontend")
public class FrontendResource {
    @Inject
    @RestClient
    RestClientWithOidcClientFilter restClientWithOidcClientFilter;

    @Inject
    @RestClient
    RestClientWithTokenPropagationFilter restClientWithTokenPropagationFilter;

    @GET
    @Path("user-name-with-oidc-client-token")
    @Produces("text/plain")
    public Uni<String> getUserNameWithOidcClientToken() {
        return restClientWithOidcClientFilter.getUserName();
    }
    
    @GET
    @Path("admin-name-with-oidc-client-token")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithOidcClientToken() {
	    return restClientWithOidcClientFilter.getAdminName();
    }
    
    @GET
    @Path("user-name-with-propagated-token")
    @Produces("text/plain")
    public Uni<String> getUserNameWithPropagatedToken() {
        return restClientWithTokenPropagationFilter.getUserName();
    }
    
    @GET
    @Path("admin-name-with-propagated-token")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithPropagatedToken() {
        return restClientWithTokenPropagationFilter.getAdminName();
    }
}

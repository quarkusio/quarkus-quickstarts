package org.acme.security.openid.connect.client;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;

@Path("/frontend")
public class FrontendResource {
    @Inject
    @RestClient
    ProtectedResourceOidcClientFilter protectedResourceOidcClientFilter;

    @Inject
    @RestClient
    ProtectedResourceTokenPropagationFilter protectedResourceTokenPropagationFilter;

    @GET
    @Path("user-name-with-oidc-client-token")
    @Produces("text/plain")
    public Uni<String> getUserNameWithOidcClientToken() {
        return protectedResourceOidcClientFilter.getUserName();
    }
    
    @GET
    @Path("admin-name-with-oidc-client-token")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithOidcClientToken() {
	    return protectedResourceOidcClientFilter.getAdminName();
    }
    
    @GET
    @Path("user-name-with-propagated-token")
    @Produces("text/plain")
    public Uni<String> getUserNameWithPropagatedToken() {
        return protectedResourceTokenPropagationFilter.getUserName();
    }
    
    @GET
    @Path("admin-name-with-propagated-token")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithPropagatedToken() {
        return protectedResourceTokenPropagationFilter.getAdminName();
    }
}

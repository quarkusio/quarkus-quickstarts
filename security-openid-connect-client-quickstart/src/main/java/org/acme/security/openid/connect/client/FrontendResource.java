package org.acme.security.openid.connect.client;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.oidc.client.Tokens;
import io.quarkus.oidc.client.runtime.TokensHelper;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/frontend")
public class FrontendResource {
    @Inject 
    OidcClientCreator oidcClientCreator;
    TokensHelper tokenHelper = new TokensHelper();
    
    @Inject
    @RestClient
    RestClientWithOidcClientFilter restClientWithOidcClientFilter;

    @Inject
    @RestClient
    RestClientWithTokenHeaderParam restClientWithTokenHeaderParam;

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
    @Path("user-name-with-oidc-client-token-header-param")
    @Produces("text/plain")
    public Uni<String> getUserNameWithOidcClientTokenHeaderParam() {
    	return tokenHelper.getTokens(oidcClientCreator.getOidcClient()).onItem()
        		.transformToUni(tokens -> restClientWithTokenHeaderParam.getUserName("Bearer " + tokens.getAccessToken()));
    }
    
    @GET
    @Path("admin-name-with-oidc-client-token-header-param")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithOidcClientTokenHeaderParam() {
    	return tokenHelper.getTokens(oidcClientCreator.getOidcClient()).onItem()
        		.transformToUni(tokens -> restClientWithTokenHeaderParam.getAdminName("Bearer " + tokens.getAccessToken()));
    }
    
    @GET
    @Path("user-name-with-oidc-client-token-header-param-blocking")
    @Produces("text/plain")
    public String getUserNameWithOidcClientTokenHeaderParamBlocking() {
    	Tokens tokens = tokenHelper.getTokens(oidcClientCreator.getOidcClient()).await().indefinitely();
        return restClientWithTokenHeaderParam.getUserName("Bearer " + tokens.getAccessToken()).await().indefinitely();
    }
    
    @GET
    @Path("admin-name-with-oidc-client-token-header-param-blocking")
    @Produces("text/plain")
    public String getAdminNameWithOidcClientTokenHeaderParamBlocking() {
    	Tokens tokens = tokenHelper.getTokens(oidcClientCreator.getOidcClient()).await().indefinitely();
        return restClientWithTokenHeaderParam.getAdminName("Bearer " + tokens.getAccessToken()).await().indefinitely();
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

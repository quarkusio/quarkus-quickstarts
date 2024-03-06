package org.acme.security.openid.connect.client;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.OidcClientConfig;
import io.quarkus.oidc.client.OidcClientConfig.Grant.Type;
import io.quarkus.oidc.client.OidcClients;
import io.quarkus.oidc.client.Tokens;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/frontend")
public class FrontendResource {
    @Inject 
    OidcClients oidcClients;

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String oidcProviderAddress;

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
    	Uni<OidcClient> oidcClient = createOidcClientDynamically(); 
        return oidcClient.onItem()
        		.transformToUni(client -> client.getTokens().onItem()
        		.transformToUni(tokens -> restClientWithTokenHeaderParam.getUserName("Bearer " + tokens.getAccessToken())));
    }
    
    @GET
    @Path("admin-name-with-oidc-client-token-header-param")
    @Produces("text/plain")
    public Uni<String> getAdminNameWithOidcClientTokenHeaderParam() {
    	Uni<OidcClient> oidcClient = createOidcClientDynamically();
    	return oidcClient.onItem()
        		.transformToUni(client -> client.getTokens().onItem()
        		.transformToUni(tokens -> restClientWithTokenHeaderParam.getAdminName("Bearer " + tokens.getAccessToken())));
    }
    
    @GET
    @Path("user-name-with-oidc-client-token-header-param-blocking")
    @Produces("text/plain")
    public String getUserNameWithOidcClientTokenHeaderParamBlocking() {
    	Uni<OidcClient> oidcClient = createOidcClientDynamically(); 
    	OidcClient client = oidcClient.await().indefinitely();
        Tokens tokens = client.getTokens().await().indefinitely();
        return restClientWithTokenHeaderParam.getUserName("Bearer " + tokens.getAccessToken()).await().indefinitely();
    }
    
    @GET
    @Path("admin-name-with-oidc-client-token-header-param-blocking")
    @Produces("text/plain")
    public String getAdminNameWithOidcClientTokenHeaderParamBlocking() {
    	Uni<OidcClient> oidcClient = createOidcClientDynamically();
    	OidcClient client = oidcClient.await().indefinitely();
        Tokens tokens = client.getTokens().await().indefinitely();
        return restClientWithTokenHeaderParam.getAdminName("Bearer " + tokens.getAccessToken()).await().indefinitely();
    }
    
    private Uni<OidcClient> createOidcClientDynamically() {
    	OidcClientConfig cfg = new OidcClientConfig();
        cfg.setId("myclient");
        cfg.setAuthServerUrl(oidcProviderAddress);
        cfg.setClientId("backend-service");
        cfg.getCredentials().setSecret("secret");
        cfg.getGrant().setType(Type.PASSWORD);
        cfg.setGrantOptions(Map.of("password",
        		Map.of("username", "alice", "password", "alice")));
        return oidcClients.newClient(cfg);
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

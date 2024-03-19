package org.acme.security.openid.connect.client;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.OidcClientConfig;
import io.quarkus.oidc.client.OidcClientConfig.Grant.Type;
import io.quarkus.oidc.client.OidcClients;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class OidcClientCreator {

    @Inject
    OidcClients oidcClients;
    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String oidcProviderAddress;

    private volatile OidcClient oidcClient;

    public void startup(@Observes StartupEvent event) {
    	createOidcClient().subscribe().with(client -> {oidcClient = client;});
    }

    public OidcClient getOidcClient() {
        return oidcClient;
    }

    private Uni<OidcClient> createOidcClient() {
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
}
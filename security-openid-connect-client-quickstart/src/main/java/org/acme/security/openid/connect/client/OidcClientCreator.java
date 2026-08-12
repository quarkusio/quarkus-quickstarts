package org.acme.security.openid.connect.client;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.OidcClientConfigBuilder;
import io.quarkus.oidc.client.OidcClients;
import io.quarkus.oidc.client.runtime.OidcClientConfig;
import io.quarkus.oidc.client.runtime.OidcClientConfig.Grant.Type;
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
        OidcClientConfig cfg = new OidcClientConfigBuilder()
                .id("myclient")
                .authServerUrl(oidcProviderAddress)
                .clientId("backend-service")
                .credentials("secret")
                .grant(Type.PASSWORD)
                .grantOptions("password", Map.of("username", "alice", "password", "alice"))
                .build();
        return oidcClients.newClient(cfg);
    }
}
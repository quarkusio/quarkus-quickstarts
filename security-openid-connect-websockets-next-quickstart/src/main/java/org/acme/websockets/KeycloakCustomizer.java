package org.acme.websockets;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;

import java.util.List;

public class KeycloakCustomizer {

    void customizeKeycloakOnStartup(@Observes StartupEvent ignored, Keycloak keycloak) {
        try {
            // Make access token expire in one minute, so that we can quickly observe token refresh
            var quarkusRealmResource = keycloak.realm("quarkus");
            var quarkusRealmRepresentation = quarkusRealmResource.toRepresentation();
            quarkusRealmRepresentation.setAccessTokenLifespan(60);
            quarkusRealmResource.update(quarkusRealmRepresentation);

            // Create new OIDC client for WS client
            // Keycloak JS adapter only supports public clients as there is nowhere to save credentials on the front end.
            ClientRepresentation client = new ClientRepresentation();
            client.setClientId("websockets-js-client");
            client.setRedirectUris(List.of("*"));
            client.setPublicClient(true);
            client.setDirectAccessGrantsEnabled(true);
            client.setServiceAccountsEnabled(true);
            client.setEnabled(true);
            client.setRedirectUris(List.of("*"));
            client.setDefaultClientScopes(List.of("microprofile-jwt", "basic"));
            // Make Keycloak set 'Access-Control-Allow-Origin' to Quarkus application origin so that token request pass
            client.setWebOrigins(List.of("http://localhost:8080"));
            try (var response = keycloak.realm("quarkus").clients().create(client)) {
                if (response.getStatus() != 201) {
                    throw new RuntimeException("Keycloak client creation failed with status " + response.getStatus());
                }
            }
        } catch (Exception e) {
            Log.error("Failed to make customize Keycloak instance", e);
        }
    }

}

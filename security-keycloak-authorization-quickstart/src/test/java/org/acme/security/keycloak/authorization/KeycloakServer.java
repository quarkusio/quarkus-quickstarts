package org.acme.security.keycloak.authorization;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class KeycloakServer implements BeforeAllCallback, AfterAllCallback {

    private GenericContainer keycloak;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        keycloak = new FixedHostPortGenericContainer("quay.io/keycloak/keycloak:7.0.1")
                .withFixedExposedPort(8180, 8080)
                .withEnv("KEYCLOAK_USER", "admin")
                .withEnv("KEYCLOAK_PASSWORD", "admin")
                .withEnv("KEYCLOAK_IMPORT", "/tmp/realm.json")
                .withClasspathResourceMapping("quarkus-realm.json", "/tmp/realm.json", BindMode.READ_ONLY)
                .waitingFor(Wait.forHttp("/auth"));
        keycloak.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        keycloak.stop();
    }
}

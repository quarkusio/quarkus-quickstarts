package org.acme.quickstart.oidc;

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
        keycloak = new FixedHostPortGenericContainer("quay.io/keycloak/keycloak:" + System.getProperty("keycloak.version"))
                .withFixedExposedPort(8180, 8080)
                .withEnv("KEYCLOAK_USER", "admin")
                .withEnv("KEYCLOAK_PASSWORD", "admin")
                .withEnv("KEYCLOAK_IMPORT", "/tmp/default-tenant-realm.json,/tmp/tenant-a-realm.json")
                .withClasspathResourceMapping("default-tenant-realm.json", "/tmp/default-tenant-realm.json", BindMode.READ_ONLY)
                .withClasspathResourceMapping("tenant-a-realm.json", "/tmp/tenant-a-realm.json", BindMode.READ_ONLY)
                .waitingFor(Wait.forHttp("/auth"));
        keycloak.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        keycloak.stop();
    }
}

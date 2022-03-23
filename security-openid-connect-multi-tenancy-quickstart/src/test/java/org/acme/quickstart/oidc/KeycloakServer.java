package org.acme.quickstart.oidc;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.SelinuxContext;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Collections;
import java.util.Map;

public class KeycloakServer implements QuarkusTestResourceLifecycleManager {

    private GenericContainer keycloak;

    @Override
    public Map<String, String> start() {
        keycloak = new FixedHostPortGenericContainer("quay.io/keycloak/keycloak:" + System.getProperty("keycloak.version"))
                .withFixedExposedPort(8180, 8080)
                .withEnv("DB_VENDOR", "H2")
                .withEnv("KEYCLOAK_USER", "admin")
                .withEnv("KEYCLOAK_PASSWORD", "admin")
                .withEnv("KEYCLOAK_IMPORT", "/tmp/default-tenant-realm.json,/tmp/tenant-a-realm.json")
                .withClasspathResourceMapping("default-tenant-realm.json", "/tmp/default-tenant-realm.json", BindMode.READ_ONLY, SelinuxContext.SINGLE)
                .withClasspathResourceMapping("tenant-a-realm.json", "/tmp/tenant-a-realm.json", BindMode.READ_ONLY, SelinuxContext.SINGLE)
                .waitingFor(Wait.forHttp("/auth"));
        keycloak.start();
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        keycloak.stop();
    }
}

package org.acme.security.openid.connect.web.authentication;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KeycloakServer implements QuarkusTestResourceLifecycleManager {

    private GenericContainer keycloak;

    @Override
    public Map<String, String> start() {

        keycloak = new FixedHostPortGenericContainer("quay.io/keycloak/keycloak:" + System.getProperty("keycloak.version"))
                .withFixedExposedPort(8180, 8080)
                .withEnv("DB_VENDOR", "H2")
                .withEnv("KEYCLOAK_USER", "admin")
                .withEnv("KEYCLOAK_PASSWORD", "admin")
                .withEnv("KEYCLOAK_IMPORT", "/tmp/realm.json")
                .withClasspathResourceMapping("quarkus-realm.json", "/tmp/realm.json", BindMode.READ_ONLY)
                .waitingFor(Wait.forHttp("/auth"));
        keycloak.start();
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        keycloak.stop();
    }
}

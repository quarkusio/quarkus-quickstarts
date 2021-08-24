package org.acme.security.openid.connect;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
public class NativeBearerTokenAuthenticationIT extends BearerTokenAuthenticationTest {

    QuarkusIntegrationTest.Context context;

    @Override
    protected String getServerAddress() {
        return context.devServicesProperties().get("quarkus.oidc.auth-server-url");
    }
}

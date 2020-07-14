package org.acme.security.oauth2;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

public class MockAuthorizationServerTestResource implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        // define the mock for the introspect endpoint
        WireMock.stubFor(WireMock.post("/introspect").willReturn(WireMock.aResponse()
                .withBody(
                        "{\"active\":true,\"scope\":\"READER\",\"username\":null,\"iat\":1562315654,\"exp\":1562317454,\"expires_in\":1458,\"client_id\":\"my_client_id\"}")));


        return Collections.singletonMap("quarkus.oauth2.introspection-url", wireMockServer.baseUrl() + "/introspect");
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}

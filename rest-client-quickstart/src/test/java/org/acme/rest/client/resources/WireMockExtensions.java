package org.acme.rest.client.resources;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WireMockExtensions implements QuarkusTestResourceLifecycleManager {

    private static final String COUNTRIES_JSON_FILE = "/extensions.json";
    private static final String BASE_PATH = "/api";
    private static final int WIREMOCK_PORT = 7777;

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(WIREMOCK_PORT);
        wireMockServer.start();
        stubExtensions();
        return Collections.singletonMap("quarkus.rest-client.\"org.acme.rest.client.ExtensionsService\".url",
                wireMockServer.baseUrl() + BASE_PATH);
    }

    @Override
    public void stop() {
        if (Objects.nonNull(wireMockServer))
            wireMockServer.stop();
    }

    private void stubExtensions() {

        try (InputStream is = WireMockExtensions.class.getResourceAsStream(COUNTRIES_JSON_FILE)) {
            String extensions = new String(is.readAllBytes());

            // Stub for full list of extensions:
            wireMockServer.stubFor(get(urlEqualTo(BASE_PATH))
                    .willReturn(okJson(extensions)));

            // Stub for each country
            try (StringReader sr = new StringReader(extensions);
                    JsonParser parser = Json.createParser(sr)) {
                parser.next();
                for (JsonValue extension : parser.getArray()) {
                    String id = extension.asJsonObject().getString("id");

                    wireMockServer.stubFor(get(urlEqualTo(BASE_PATH + "/extensions?id=" + URLEncoder.encode(id, StandardCharsets.UTF_8)))
                            .willReturn(okJson("[" + extension + "]")));
                }
            }

        } catch (IOException e) {
            fail("Could not configure Wiremock server. Caused by: " + e.getMessage());
        }
    }
}

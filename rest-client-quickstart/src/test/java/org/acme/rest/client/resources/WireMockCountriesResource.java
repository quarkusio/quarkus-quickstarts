package org.acme.rest.client.resources;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WireMockCountriesResource implements QuarkusTestResourceLifecycleManager {

    private static final String COUNTRIES_JSON_FILE = "/countries.json";
    private static final String BASE_PATH = "/rest";
    private static final int WIREMOCK_PORT = 7777;

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(WIREMOCK_PORT);
        wireMockServer.start();
        stubCountries();
        return Collections.singletonMap("org.acme.rest.client.CountriesService/mp-rest/url",
                wireMockServer.baseUrl() + BASE_PATH);
    }

    @Override
    public void stop() {
        if (Objects.nonNull(wireMockServer))
            wireMockServer.stop();
    }

    private void stubCountries() {

        try (InputStream is = WireMockCountriesResource.class.getResourceAsStream(COUNTRIES_JSON_FILE)) {
            String countries = new String(is.readAllBytes());

            // Stub for full list of countries:
            wireMockServer.stubFor(get(urlEqualTo(BASE_PATH))
                    .willReturn(okJson(countries)));

            // Stub for each country
            try (StringReader sr = new StringReader(countries);
                    JsonParser parser = Json.createParser(sr)) {
                parser.next();
                for (JsonValue country : parser.getArray()) {
                    String name = country.asJsonObject().getString("name");

                    wireMockServer.stubFor(get(urlEqualTo(BASE_PATH + "/v2/name/" + name))
                            .willReturn(okJson("[" + country + "]")));
                }
            }

        } catch (IOException e) {
            fail("Could not configure Wiremock server. Caused by: " + e.getMessage());
        }
    }
}

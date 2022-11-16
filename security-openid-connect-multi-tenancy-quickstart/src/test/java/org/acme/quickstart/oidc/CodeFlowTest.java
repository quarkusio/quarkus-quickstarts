package org.acme.quickstart.oidc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;

@QuarkusTest
public class CodeFlowTest {

	KeycloakTestClient keycloakClient = new KeycloakTestClient();
	
    @Test
    public void testLogInDefaultTenant() throws IOException {
        try (final WebClient webClient = createWebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/default");

            assertEquals("Sign in to quarkus", page.getTitleText());

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertTrue(page.asText().contains("tenant"));
        }
    }

    @Test
    public void testLogInTenantAWebApp() throws IOException {
        try (final WebClient webClient = createWebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/tenant-a");

            assertEquals("Sign in to tenant-a", page.getTitleText());

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertTrue(page.asText().contains("alice@tenant-a.org"));
        }
    }
    
    @Test
    public void testLogInTenantABearerToken() throws IOException {
    	RestAssured.given().auth().oauth2(getAccessToken()).when()
    	   .get("/tenant-a/bearer").then().body(containsString("alice@tenant-a.org"));
    }

    private String getAccessToken() {
	return keycloakClient.getRealmAccessToken("tenant-a", "alice", "alice", "multi-tenant-client", "secret");
    }

    @Test
    public void testReAuthenticateWhenSwitchingTenants() throws IOException {
        try (final WebClient webClient = createWebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/tenant-a");

            assertEquals("Sign in to tenant-a", page.getTitleText());

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertTrue(page.asText().contains("alice@tenant-a.org"));

            page = webClient.getPage("http://localhost:8081/default");

            assertEquals("Sign in to quarkus", page.getTitleText());

            loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertTrue(page.asText().contains("alice@keycloak.org"));
        }
    }

    private WebClient createWebClient() {
        WebClient webClient = new WebClient();
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        return webClient;
    }
}

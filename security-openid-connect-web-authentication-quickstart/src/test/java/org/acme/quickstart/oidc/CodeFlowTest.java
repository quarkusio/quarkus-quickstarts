package org.acme.quickstart.oidc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@ExtendWith(KeycloakServer.class)
public class CodeFlowTest {

    @Test
    public void testCodeFlowNoConsent() throws IOException {
        try (final WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Log in to quarkus", page.getTitleText());

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertEquals("Welcome to Your Quarkus Application", page.getTitleText());

            page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Welcome to Your Quarkus Application", page.getTitleText(),
                    "A second request should not redirect and just re-authenticate the user");
        }
    }

    @Test
    public void testTokenTimeoutLogout() throws IOException, InterruptedException {
        try (final WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Log in to quarkus", page.getTitleText());

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertEquals("Welcome to Your Quarkus Application", page.getTitleText());

            Thread.sleep(5000);

            page = webClient.getPage("http://localhost:8081/index.html");

            Cookie sessionCookie = getSessionCookie(webClient);

            assertNull(sessionCookie);

            page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Log in to quarkus", page.getTitleText());
        }
    }

    @Test
    public void testTokenInjection() throws IOException {
        try (final WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Log in to quarkus", page.getTitleText());

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertEquals("Welcome to Your Quarkus Application", page.getTitleText());

            page = webClient.getPage("http://localhost:8081/tokens");

            assertTrue(page.getBody().asText().contains("username"));
            assertTrue(page.getBody().asText().contains("scopes"));
            assertTrue(page.getBody().asText().contains("refresh_token: true"));
        }
    }

    private Cookie getSessionCookie(WebClient webClient) {
        return webClient.getCookieManager().getCookie("q_session");
    }
}

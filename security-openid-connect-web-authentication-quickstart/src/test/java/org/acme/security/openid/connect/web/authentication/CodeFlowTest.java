package org.acme.security.openid.connect.web.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CodeFlowTest {

    @Test
    public void testCodeFlowNoConsent() throws IOException {
        try (final WebClient webClient = createWebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Sign in to quarkus", page.getTitleText());
            List<Cookie> stateCookies = getStateCookies(webClient); 
            assertNotNull(stateCookies);
            assertEquals(1, stateCookies.size());
            
            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertEquals("Welcome to Your Quarkus Application", page.getTitleText());

            page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Welcome to Your Quarkus Application", page.getTitleText(),
                    "A second request should not redirect and just re-authenticate the user");
            assertNotNull(getSessionCookie(webClient));
            assertNull(getStateCookies(webClient));
            
            webClient.getCookieManager().clearCookies();
        }
    }

    @Test
    public void testTokenTimeoutLogout() throws IOException, InterruptedException {
        try (final WebClient webClient = createWebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Sign in to quarkus", page.getTitleText());
            List<Cookie> stateCookies = getStateCookies(webClient); 
            assertNotNull(stateCookies);
            assertEquals(1, stateCookies.size());
            
            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertEquals("Welcome to Your Quarkus Application", page.getTitleText());

            Cookie sessionCookie = getSessionCookie(webClient);
            assertNotNull(sessionCookie);
            assertNull(getStateCookies(webClient));

            page = webClient.getPage("http://localhost:8081/index.html");
            assertEquals("Welcome to Your Quarkus Application", page.getTitleText());

            // The same session cookie value is expected after 2 consecutive calls
            assertEquals(sessionCookie.getValue(), getSessionCookie(webClient).getValue());

            // Session refresh skew is 7 seconds, ID token lifespan is 9 seconds, therefore a new session
            // must be automatically created after waiting for 3 seconds
            Thread.sleep(3000);

            page = webClient.getPage("http://localhost:8081/index.html");
            assertEquals("Welcome to Your Quarkus Application", page.getTitleText());
            Cookie refreshSkewSessionCookie = getSessionCookie(webClient);
            assertNotEquals(sessionCookie.getValue(), refreshSkewSessionCookie.getValue());

            // Lets wait till the session cookie itself has expired who age is ID token 9 secs plus session age extension 3 secs = 12 secs
            Thread.sleep(13000);

            // Re-authentication request is expected
            page = webClient.getPage("http://localhost:8081/index.html");

            assertNull(getSessionCookie(webClient));

            assertEquals("Sign in to quarkus", page.getTitleText());

            stateCookies = getStateCookies(webClient); 
            assertNotNull(stateCookies);
            assertEquals(1, stateCookies.size());

            webClient.getCookieManager().clearCookies();
        }
    }

    @Test
    public void testTokenInjection() throws IOException {
        try (final WebClient webClient = createWebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/index.html");

            assertEquals("Sign in to quarkus", page.getTitleText());
            List<Cookie> stateCookies = getStateCookies(webClient); 
            assertNotNull(stateCookies);
            assertEquals(1, stateCookies.size());
            assertNull(getSessionCookie(webClient));

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute("alice");
            loginForm.getInputByName("password").setValueAttribute("alice");

            page = loginForm.getInputByName("login").click();

            assertEquals("Welcome to Your Quarkus Application", page.getTitleText());

            page = webClient.getPage("http://localhost:8081/tokens");

            assertTrue(page.getBody().asText().contains("username"));
            assertTrue(page.getBody().asText().contains("scopes"));
            assertTrue(page.getBody().asText().contains("refresh_token: true"));
            
            assertNotNull(getSessionCookie(webClient));
            assertNull(getStateCookies(webClient));
            
            webClient.getCookieManager().clearCookies();
        }
    }

    private Cookie getSessionCookie(WebClient webClient) {
        return webClient.getCookieManager().getCookie("q_session");
    }
    
    private List<Cookie> getStateCookies(WebClient webClient) {
        List<Cookie> cookies = webClient.getCookieManager().getCookies().stream().filter(c -> c.getName().startsWith("q_auth"))
        		.collect(Collectors.toList());
        return cookies.isEmpty() ? null : cookies;
    }

    private WebClient createWebClient() {
        WebClient webClient = new WebClient();

        webClient.setCssErrorHandler(new SilentCssErrorHandler());

        return webClient;
    }

}

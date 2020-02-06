package org.acme.security.jwt;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

import javax.json.Json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.DisabledOnNativeImage;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * Tests of the TokenSecuredResource REST endpoints
 */
@QuarkusTest
public class TokenSecuredResourceTest {

    /**
     * The test generated JWT token string
     */
    private String token;

    @BeforeEach
    public void generateToken() throws Exception {
        HashMap<String, Long> timeClaims = new HashMap<>();
        token = TokenUtils.generateTokenString("/JwtClaims.json", timeClaims);
    }

    @Test
    public void testHelloEndpoint() {
        Response response = given()
                .when()
                .get("/secured/permit-all")
                .andReturn();

        response.then()
                .statusCode(200)
                .body(containsString("hello + anonymous, isSecure: false, authScheme: null, hasJWT: false"));
    }

    @Test
    public void testHelloRolesAllowed() {
        Response response = given().auth()
                .oauth2(token)
                .when()
                .get("/secured/roles-allowed").andReturn();

        response.then()
                .statusCode(200)
                .body(containsString("hello + jdoe@quarkus.io, isSecure: false, authScheme: Bearer, hasJWT: true"));
    }

    @Test
    public void testHelloDenyAll() {
        Response response = given().auth()
                .oauth2(token)
                .when()
                .get("/secured/deny-all").andReturn();

        Assertions.assertEquals(HttpURLConnection.HTTP_FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testWinners() {
        Response response = RestAssured.given().auth()
                .oauth2(token)
                .when()
                .get("/secured/winners").andReturn();

        Assertions.assertFalse(response.body().asString().isEmpty());
    }

    @Test
    public void testWinnersWithBirthdate() {
        Response response = RestAssured.given().auth()
                .oauth2(token)
                .when()
                .get("/secured/winners2").andReturn();

        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
        Assertions.assertFalse(response.body().asString().isEmpty());
    }

    @Test
    @DisabledOnNativeImage("Doesn't work in the native mode due to a subresource issue")
    public void testLottoWinners() {
        Response response = RestAssured.given().auth()
                .oauth2(token)
                .when()
                .get("/secured/lotto/winners").andReturn();

        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
        String replyString = response.body().asString();
        Json.createReader(new StringReader(replyString)).readObject();
        LottoNumbers numbers = response.as(LottoNumbers.class);
        Assertions.assertFalse(numbers.numbers.isEmpty());
    }
}

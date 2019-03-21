import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingEndpointTest {

    @Test
    public void testGreeter() {
        given()
                .when().get("/hello/Tim")
                .then()
                .statusCode(200)
                .body(startsWith("Hello Tim!"));
    }

    @Test
    public void testEventBusGreeter() {
        given()
                .when().get("/hello/Tim/messaging")
                .then()
                .statusCode(200)
                .body(startsWith("Hello Tim!"));
    }

}

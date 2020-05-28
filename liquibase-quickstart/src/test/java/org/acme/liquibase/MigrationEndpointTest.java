package org.acme.liquibase;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class MigrationEndpointTest {

    @Test
    public void testListAllChangeStatus() {
        given()
                .when().get("/migration/status")
                .then()
                .statusCode(200)
                .body(containsString("previouslyRan"),
                        containsString("storedCheckSum"),
                        containsString("willRun"));
    }

}

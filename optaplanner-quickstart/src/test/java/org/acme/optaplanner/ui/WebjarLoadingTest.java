package org.acme.optaplanner.ui;

import io.quarkus.test.junit.DisabledOnNativeImage;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class WebjarLoadingTest {

    @Test
    public void getBootstrapCss() {
        given()
                .when().get("webjars/bootstrap/4.3.1/css/bootstrap.min.css")
                .then()
                .statusCode(200);
    }

}

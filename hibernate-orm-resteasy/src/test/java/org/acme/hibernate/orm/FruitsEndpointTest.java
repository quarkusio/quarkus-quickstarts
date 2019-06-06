package org.acme.hibernate.orm;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@QuarkusTest
@Testcontainers
class FruitsEndpointTest {

    @Container
    static PostgreSQLContainer DATABASE = new PostgreSQLContainer<>("postgres:10.5")
            .withDatabaseName("quarkus_test")
            .withUsername("quarkus_test")
            .withPassword("quarkus_test")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd ->
                    cmd
                    .withHostName("localhost")
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432))));

    @Test
    void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
        given()
              .when().get("/fruits")
              .then()
              .statusCode(200)
              .body(
                    containsString("Cherry"),
                    containsString("Apple"),
                    containsString("Banana")
                    );

        //Delete the Cherry:
        given()
              .when().delete("/fruits/1")
              .then()
              .statusCode(204)
              ;

        //List all, cherry should be missing now:
        given()
              .when().get("/fruits")
              .then()
              .statusCode(200)
              .body(
                    not( containsString("Cherry") ),
                    containsString("Apple"),
                    containsString("Banana")
              );
    }

}

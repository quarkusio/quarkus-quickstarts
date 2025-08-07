package org.acme;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

@QuarkusTest
@WithTestResource(ConsulTestResource.class)
public class RegistrationTest {

    @Inject
    @ConfigProperty(name = "consul.host")
    String consulHost;

    @Inject
    @ConfigProperty(name = "consul.port")
    String consulPort;


    @Test
    public void test() {
           String consulUrl = "http://" + consulHost + ":" + consulPort;
           // curl -X GET http://127.0.0.1:8500/v1/agent/service/red
           RestAssured.get(consulUrl+"/v1/agent/service/red-service").then().statusCode(200).body(containsString("\"Service\": \"red-service\""));

    }


}

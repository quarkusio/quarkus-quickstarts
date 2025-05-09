package org.acme;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.smallrye.stork.Stork;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

@QuarkusTest
@WithTestResource(ConsulTestResource.class)
public class RegistrationTest {


       @Test
    public void test() {
           // curl -X GET http://127.0.0.1:8500/v1/agent/service/red
           RestAssured.get("http://127.0.0.1:8500/v1/agent/service/red").then().statusCode(200).body(containsString("red"), containsString("my-service"));

    }


}

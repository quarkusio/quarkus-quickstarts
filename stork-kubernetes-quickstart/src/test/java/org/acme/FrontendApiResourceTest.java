package org.acme;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.EndpointAddress;
import io.fabric8.kubernetes.api.model.EndpointAddressBuilder;
import io.fabric8.kubernetes.api.model.EndpointPortBuilder;
import io.fabric8.kubernetes.api.model.EndpointSubsetBuilder;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.EndpointsBuilder;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.KubernetesTestServer;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@WithKubernetesTestServer
@QuarkusTest
public class FrontendApiResourceTest {

    @KubernetesTestServer
    KubernetesServer mockServer;

    @BeforeEach
    public void before() {

        Map<String, String> labels = new HashMap<>();
        labels.put("type","color");
        labels.put("app.kubernetes.io/version","1.0");

        final Pod pod = new PodBuilder().withNewSpec().endSpec().withNewMetadata().withName("red").withLabels(labels).and().build();

        //Since we are using a mockServer, we are not able to make any real application running in there, so we run it locally and configure the k8s endpoint to return `localhost`
        // as IP. This way, we will send the request to localhost where the RedService is actually running.
        String[] ips = { "localhost"};
        List<EndpointAddress> endpointAddresses = Arrays.stream(ips)
                .map(ipAddress -> {
                            ObjectReference targetRef = new ObjectReference(null, null, "Pod",
                                    "red", "development", null, UUID.randomUUID().toString());
                            EndpointAddress endpointAddress = new EndpointAddressBuilder().withIp(ipAddress).withTargetRef(targetRef)
                                    .build();
                            return endpointAddress;
                        }).collect(Collectors.toList());

        Endpoints endpoint = new EndpointsBuilder()
                .withNewMetadata().withName("color-service").endMetadata()
                .addToSubsets(new EndpointSubsetBuilder().withAddresses(endpointAddresses)
                        .addToPorts(new EndpointPortBuilder().withPort(9001).build())
                        .build())
                .build();

        // Set up Kubernetes so that our "pretend" pods and endpoints are created
        mockServer.getClient().endpoints().inNamespace("development").withName("color-service").create(endpoint);
        mockServer.getClient().pods().inNamespace("development").create(pod);
    }

    @Test
    public void testApiEndpoint() {

        given()
                .when().get("/api")
                .then()
                .statusCode(200)
                .body(is("Hello from Red!"));
    }


}

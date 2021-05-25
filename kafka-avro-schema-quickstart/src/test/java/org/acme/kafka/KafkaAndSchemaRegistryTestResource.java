package org.acme.kafka;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.strimzi.StrimziKafkaContainer;

public class KafkaAndSchemaRegistryTestResource implements QuarkusTestResourceLifecycleManager {

    private final StrimziKafkaContainer kafka = new StrimziKafkaContainer();

    private GenericContainer<?> registry;

    @Override
    public Map<String, String> start() {
        kafka.start();
        registry = new GenericContainer<>("apicurio/apicurio-registry-mem:2.0.0.Final")
                .withExposedPorts(8080)
                .withEnv("QUARKUS_PROFILE", "prod");
        registry.start();
        Map<String, String> properties = new HashMap<>();
        properties.put("mp.messaging.connector.smallrye-kafka.apicurio.registry.url",
                "http://" + registry.getContainerIpAddress() + ":" + registry.getMappedPort(8080) + "/apis/registry/v2");
        properties.put("kafka.bootstrap.servers", kafka.getBootstrapServers());
        return properties;
    }

    @Override
    public void stop() {
        registry.stop();
        kafka.stop();
    }
}

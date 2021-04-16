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
        registry = new GenericContainer<>("apicurio/apicurio-registry-mem:1.3.2.Final")
                .withExposedPorts(8080)
                .withEnv("QUARKUS_PROFILE", "prod")
                .withEnv("KAFKA_BOOTSTRAP_SERVERS", kafka.getBootstrapServers())
                .withEnv("APPLICATION_ID", "registry_id")
                .withEnv("APPLICATION_SERVER", "localhost:9000");

        registry.start();
        Map<String, String> properties = new HashMap<>();
        properties.put("mp.messaging.connector.smallrye-kafka.apicurio.registry.url",
                "http://" + registry.getContainerIpAddress() + ":" + registry.getMappedPort(8080) + "/api");
        properties.put("kafka.bootstrap.servers", kafka.getBootstrapServers());
        return properties;
    }

    @Override
    public void stop() {
        registry.stop();
        kafka.stop();
    }
}
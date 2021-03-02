package org.acme.kafka;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.KafkaContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.utility.DockerImageName;

public class KafkaResource implements QuarkusTestResourceLifecycleManager {

    DockerImageName dockerImageName = DockerImageName.parse("confluentinc/cp-kafka");

    private final KafkaContainer kafka = new KafkaContainer(dockerImageName);

    @Override
    public Map<String, String> start() {
        kafka.start();
        return Collections.singletonMap("kafka.bootstrap.servers", kafka.getBootstrapServers());
    }

    @Override
    public void stop() {
        kafka.close();
    }
}

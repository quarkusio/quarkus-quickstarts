package org.acme.kafka.streams.aggregator.streams;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Map;

public class KafkaResource implements QuarkusTestResourceLifecycleManager {

    private final static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @Override
    public Map<String, String> start() {
        kafka.start();
        return Collections.singletonMap("quarkus.kafka-streams.bootstrap-servers", kafka.getBootstrapServers());
    }

    @Override
    public void stop() {
        kafka.close();
    }

    public static String getBootstrapServers() {
        return kafka.getBootstrapServers();
    }
}

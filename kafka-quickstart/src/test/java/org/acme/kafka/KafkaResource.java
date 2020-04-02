package org.acme.kafka;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.KafkaContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KafkaResource implements QuarkusTestResourceLifecycleManager {

    private KafkaContainer kafka;

    @Override
    public Map<String, String> start() {
        kafka = new KafkaContainer();
        kafka.start();
        System.setProperty("kafka.bootstrap.servers", kafka.getBootstrapServers());
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        System.clearProperty("kafka.bootstrap.servers");
        if (kafka != null) {
            kafka.close();
        }
    }
}

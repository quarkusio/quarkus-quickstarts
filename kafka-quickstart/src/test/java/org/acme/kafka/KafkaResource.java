package org.acme.kafka;

import java.util.Collections;
import java.util.Map;

import io.strimzi.StrimziKafkaContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KafkaResource implements QuarkusTestResourceLifecycleManager {

    private static final String STRIMZI_VERSION = "0.19.0-kafka-2.5.0";
    private final StrimziKafkaContainer kafka = new StrimziKafkaContainer(STRIMZI_VERSION);

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

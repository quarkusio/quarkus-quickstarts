package org.acme.kafka.streams.aggregator.streams;

import io.debezium.kafka.KafkaCluster;
import io.debezium.util.Testing;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class KafkaResource implements QuarkusTestResourceLifecycleManager  {

    private KafkaCluster kafka;

    @Override
    public Map<String, String> start() {
        try {
            Properties props = new Properties();
            props.setProperty("zookeeper.connection.timeout.ms", "45000");
            props.setProperty("group.min.session.timeout.ms", "100");
            File directory = Testing.Files.createTestingDirectory("kafka-data", true);
            kafka = new KafkaCluster().withPorts(2182, 9092)
                    .addBrokers(1)
                    .usingDirectory(directory)
                    .deleteDataUponShutdown(true)
                    .withKafkaConfiguration(props)
                    .deleteDataPriorToStartup(true)
                    .startup();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        if (kafka != null) {
            kafka.shutdown();
        }
    }
}

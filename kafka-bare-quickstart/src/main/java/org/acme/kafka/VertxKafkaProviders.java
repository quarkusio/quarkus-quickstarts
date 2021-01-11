package org.acme.kafka;

import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.kafka.client.consumer.KafkaConsumer;
import io.vertx.mutiny.kafka.client.producer.KafkaProducer;
import io.vertx.mutiny.kafka.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class VertxKafkaProviders {

    @Inject
    @Named("default-kafka-broker")
    Map<String, Object> config;

    @Inject
    Vertx vertx;

    private Map<String, String> toConfig() {
        Map<String, String> cfg = new HashMap<>();
        config.forEach((k, v) -> cfg.put(k, v.toString()));
        return cfg;
    }
    
    @Produces
    KafkaConsumer<String, String> getConsumer() {
        Map<String, String> map = new HashMap<>(toConfig());
        map.put("key.deserializer", StringDeserializer.class.getName());
        map.put("value.deserializer", StringDeserializer.class.getName());
        return KafkaConsumer.create(vertx, map, String.class, String.class);
    }

    @Produces
    KafkaProducer<String, String> getProducer() {
        Map<String, String> map = new HashMap<>(toConfig());
        map.put("key.serializer", StringSerializer.class.getName());
        map.put("value.serializer", StringSerializer.class.getName());
        return KafkaProducer.create(vertx, map, String.class, String.class);
    }

    @Produces
    KafkaAdminClient getAdmin() {
        Map<String, String> copy = new HashMap<>();
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            if (AdminClientConfig.configNames().contains(entry.getKey())) {
                copy.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return KafkaAdminClient.create(vertx, copy);
    }

}

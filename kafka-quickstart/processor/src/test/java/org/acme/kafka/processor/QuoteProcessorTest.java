package org.acme.kafka.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.inject.Inject;

import org.acme.kafka.model.Quote;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.annotation.Identifier;

@QuarkusTest
public class QuoteProcessorTest {

    @Inject
    @Identifier("default-kafka-broker")
    Map<String, Object> kafkaConfig;

    KafkaProducer<String, String> quoteRequestProducer;
    KafkaConsumer<String, Quote> quoteConsumer;

    @BeforeEach
    void setUp() {
        quoteConsumer = new KafkaConsumer<>(consumerConfig(), new StringDeserializer(), new ObjectMapperDeserializer<>(Quote.class));
        quoteRequestProducer = new KafkaProducer<>(kafkaConfig, new StringSerializer(), new StringSerializer());
    }

    @AfterEach
    void tearDown() {
        quoteRequestProducer.close();
        quoteConsumer.close();
    }

    Properties consumerConfig() {
        Properties properties = new Properties();
        properties.putAll(kafkaConfig);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-id");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    @Test
    void testProcessor() {
        quoteConsumer.subscribe(Collections.singleton("quotes"));
        UUID quoteId = UUID.randomUUID();
        quoteRequestProducer.send(new ProducerRecord<>("quote-requests", quoteId.toString()));
        ConsumerRecords<String, Quote> records = quoteConsumer.poll(Duration.ofMillis(10000));
        Quote quote = records.records("quotes").iterator().next().value();
        assertEquals(quote.id, quoteId.toString());
    }
}

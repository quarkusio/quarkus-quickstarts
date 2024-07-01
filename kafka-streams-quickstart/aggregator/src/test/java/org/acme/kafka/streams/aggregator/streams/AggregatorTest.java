package org.acme.kafka.streams.aggregator.streams;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.kafka.streams.aggregator.model.Aggregation;
import org.acme.kafka.streams.aggregator.model.WeatherStation;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.acme.kafka.streams.aggregator.streams.TopologyProducer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration testing of the application with an embedded broker.
 */
@QuarkusTest
@QuarkusTestResource(KafkaResource.class)
public class AggregatorTest {

    KafkaProducer<Integer, String> temperatureProducer;
    KafkaProducer<Integer, WeatherStation> weatherStationsProducer;
    KafkaConsumer<Integer, Aggregation> weatherStationsConsumer;

    @BeforeEach
    public void setUp() {
        temperatureProducer = new KafkaProducer<>(properties(), new IntegerSerializer(), new StringSerializer());
        weatherStationsProducer = new KafkaProducer<>(properties(), new IntegerSerializer(), new ObjectMapperSerializer<>());
        weatherStationsConsumer = new KafkaConsumer<>(consumerProps(), new IntegerDeserializer(), new ObjectMapperDeserializer<>(Aggregation.class));
    }

    @AfterEach
    public void tearDown() {
        temperatureProducer.close();
        weatherStationsProducer.close();
        weatherStationsConsumer.close();
    }

    @Test
    @Timeout(value = 30)
    public void test() {
        weatherStationsConsumer.subscribe(Collections.singletonList(TEMPERATURES_AGGREGATED_TOPIC));
        weatherStationsProducer.send(new ProducerRecord<>(WEATHER_STATIONS_TOPIC, 1, new WeatherStation(1, "Station 1")));
        temperatureProducer.send(new ProducerRecord<>(TEMPERATURE_VALUES_TOPIC, 1, Instant.now() + ";" + "15"));
        temperatureProducer.send(new ProducerRecord<>(TEMPERATURE_VALUES_TOPIC, 1, Instant.now() + ";" + "25"));
        Aggregation result = poll(weatherStationsConsumer, 1).get(0).value();

        //should be: result.count == 2, but repeated local test run (without maven clean) accumulates +2
        //because kafka persistence is located on target/data
        assertEquals(0, result.count % 2);
        assertEquals(1, result.stationId);
        assertEquals("Station 1", result.stationName);
        assertEquals(20, result.avg);
    }

    private Properties consumerProps() {
        Properties props = properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-id");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    private Properties properties() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KafkaResource.getBootstrapServers());
        return props;
    }

    private List<ConsumerRecord<Integer, Aggregation>> poll(Consumer<Integer, Aggregation> consumer, int expectedRecordCount) {
        int fetched = 0;
        List<ConsumerRecord<Integer, Aggregation>> result = new ArrayList<>();
        while (fetched < expectedRecordCount) {
            ConsumerRecords<Integer, Aggregation> records = consumer.poll(Duration.ofSeconds(5));
            records.forEach(result::add);
            fetched = result.size();
        }
        return result;
    }
}

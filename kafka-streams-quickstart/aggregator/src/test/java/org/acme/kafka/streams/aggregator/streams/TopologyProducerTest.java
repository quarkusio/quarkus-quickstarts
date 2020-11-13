package org.acme.kafka.streams.aggregator.streams;

import static org.acme.kafka.streams.aggregator.streams.TopologyProducer.TEMPERATURES_AGGREGATED_TOPIC;
import static org.acme.kafka.streams.aggregator.streams.TopologyProducer.TEMPERATURE_VALUES_TOPIC;
import static org.acme.kafka.streams.aggregator.streams.TopologyProducer.WEATHER_STATIONS_STORE;
import static org.acme.kafka.streams.aggregator.streams.TopologyProducer.WEATHER_STATIONS_TOPIC;

import java.time.Instant;
import java.util.Properties;

import javax.inject.Inject;

import org.acme.kafka.streams.aggregator.model.Aggregation;
import org.acme.kafka.streams.aggregator.model.WeatherStation;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Testing of the Topology without a broker, using TopologyTestDriver
 */
@QuarkusTest
public class TopologyProducerTest {

    @Inject
    Topology topology;

    TopologyTestDriver testDriver;

    TestInputTopic<Integer, String> temperatures;

    TestInputTopic<Integer, WeatherStation> weatherStations;

    TestOutputTopic<Integer, Aggregation> temperaturesAggregated;

    @BeforeEach
    public void setUp(){
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "testApplicationId");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        testDriver = new TopologyTestDriver(topology, config);

        temperatures = testDriver.createInputTopic(TEMPERATURE_VALUES_TOPIC, new IntegerSerializer(), new StringSerializer());
        weatherStations = testDriver.createInputTopic(WEATHER_STATIONS_TOPIC, new IntegerSerializer(), new ObjectMapperSerializer());

        temperaturesAggregated = testDriver.createOutputTopic(TEMPERATURES_AGGREGATED_TOPIC, new IntegerDeserializer(),
                new ObjectMapperDeserializer<>(Aggregation.class));
    }

    @AfterEach
    public void tearDown(){
        testDriver.getTimestampedKeyValueStore(WEATHER_STATIONS_STORE).flush();
        testDriver.close();
    }

    @Test
    public void test(){
        WeatherStation station1 = new WeatherStation(1,"Station 1");
        weatherStations.pipeInput(station1.id, station1);
        temperatures.pipeInput(station1.id, Instant.now() + ";" + "15");
        temperatures.pipeInput(station1.id, Instant.now() + ";" + "25");

        temperaturesAggregated.readRecord();
        TestRecord<Integer, Aggregation> result = temperaturesAggregated.readRecord();

        Assertions.assertEquals(2, result.getValue().count);
        Assertions.assertEquals(1, result.getValue().stationId);
        Assertions.assertEquals("Station 1", result.getValue().stationName);
        Assertions.assertEquals(20, result.getValue().avg);
    }
}

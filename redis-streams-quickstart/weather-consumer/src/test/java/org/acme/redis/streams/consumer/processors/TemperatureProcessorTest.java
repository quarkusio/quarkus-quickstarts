package org.acme.redis.streams.consumer.processors;

import io.quarkus.redis.client.RedisClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.core.json.Json;
import io.vertx.redis.client.Response;
import org.acme.redis.streams.consumer.domain.Temperature;
import org.acme.redis.streams.consumer.domain.TemperatureAggregate;
import org.acme.redis.streams.consumer.domain.WeatherStation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.acme.redis.streams.consumer.util.AppConstants.AGGREGATE_TABLE;
import static org.acme.redis.streams.consumer.util.AppConstants.CONSUMER_GROUP;
import static org.acme.redis.streams.consumer.util.AppConstants.TEMPERATURE_VALUES_STREAM;
import static org.acme.redis.streams.consumer.util.AppConstants.WEATHER_STATIONS_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@QuarkusTest
@TestInstance(PER_CLASS)
@QuarkusTestResource(RedisResource.class)
public class TemperatureProcessorTest {

    private List<WeatherStation> weatherStations;

    @Inject
    RedisClient client;

    @BeforeAll
    public void init() {
        createWeatherStations();
    }

    @AfterAll
    public void cleanUp() {
        System.out.println("deleted stream: " + this.client.del(Arrays.asList(TEMPERATURE_VALUES_STREAM)));
        this.weatherStations.forEach(ws -> System.out.printf("deleted station: %d, success=%s\n", ws.id, this.client.del(Arrays.asList(WEATHER_STATIONS_TABLE + ws.id))));
    }

    @AfterEach
    public void checkPendingMessages() {
        // 0 messages should be pending e.g. all messages should have been processed from the stream
        assertThat(this.client.xpending(Arrays.asList(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP)).get(0).toString()).isEqualTo("0");
        // delete the calculated aggregates, so that we start with an empty aggregates table each test
        this.client.del(Arrays.asList(AGGREGATE_TABLE));
    }

    @Test
    public void calculate() {
        addTemperature(1L, 10.0);
        TemperatureAggregate agg = getTemperatureAggregate(1);

        System.out.println(agg);

        assertThat(agg.stationId).isEqualTo(1);
        assertThat(agg.name).isEqualTo("Hamburg");
        assertThat(agg.count).isEqualTo(1);
        assertThat(agg.avg).isEqualTo(10);
        assertThat(agg.sum).isEqualTo(10);
        assertThat(agg.max).isEqualTo(10);
        assertThat(agg.min).isEqualTo(10);
    }

    @Test
    public void calculate_multiple_temps() {
        addTemperature(1L, 10.0);
        addTemperature(2L, 0.0);
        addTemperature(1L, 15.0);
        addTemperature(2L, 20.0);
        addTemperature(2L, -5.0);

        TemperatureAggregate agg1 = getTemperatureAggregate(1);
        TemperatureAggregate agg2 = getTemperatureAggregate(2);

        System.out.println(agg1);
        System.out.println(agg2);

        assertThat(agg1.stationId).isEqualTo(1);
        assertThat(agg1.name).isEqualTo("Hamburg");
        assertThat(agg1.count).isEqualTo(2);
        assertThat(agg1.avg).isEqualTo(12.5);
        assertThat(agg1.sum).isEqualTo(25);
        assertThat(agg1.max).isEqualTo(15);
        assertThat(agg1.min).isEqualTo(10);

        assertThat(agg2.stationId).isEqualTo(2);
        assertThat(agg2.name).isEqualTo("Snowdonia");
        assertThat(agg2.count).isEqualTo(3);
        assertThat(agg2.avg).isEqualTo(5.0);
        assertThat(agg2.sum).isEqualTo(15);
        assertThat(agg2.max).isEqualTo(20);
        assertThat(agg2.min).isEqualTo(-5);
    }

    private void createWeatherStations() {
        this.weatherStations = Arrays.asList(
                new WeatherStation(1L, "Hamburg", 13.0),
                new WeatherStation(2L, "Snowdonia", -2.0),
                new WeatherStation(3L, "Tokyo", 9.0),
                new WeatherStation(4L, "Istanbul", 16.0),
                new WeatherStation(5L, "New York", 6.0),
                new WeatherStation(6L, "Marakesh", 25.0),
                new WeatherStation(7L, "Kuala Lumpur", 21.0),
                new WeatherStation(8L, "Rome", 15.0),
                new WeatherStation(9L, "Kaapstad", 28.0));

        this.weatherStations.forEach(ws -> this.client.set(Arrays.asList(WEATHER_STATIONS_TABLE + ws.id, Json.encode(ws))));
    }

    private String addTemperature(long stationId, double temperature) {
        Temperature temp = new Temperature(stationId, null, temperature, Instant.now().toString());

        Response res = this.client.xadd(Arrays.asList(
                TEMPERATURE_VALUES_STREAM,
                "maxlen",
                "~",
                "100",
                "*",
                "payload", Json.encode(temp)));

        System.out.printf("Added msg(id=%s)\n", res);
        return res.toString();
    }

    private TemperatureAggregate getTemperatureAggregate(int stationId) {
        return given()
                .accept(ContentType.JSON)
                .when()
                .log().all()
                .get("/temperatures/{id}", stationId)
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract().as(TemperatureAggregate.class);
    }
}
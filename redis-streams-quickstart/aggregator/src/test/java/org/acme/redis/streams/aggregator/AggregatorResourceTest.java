package org.acme.redis.streams.aggregator;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.stream.StreamCommands;
import io.quarkus.redis.datasource.stream.XAddArgs;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.core.json.Json;
import io.vertx.redis.client.impl.RedisClient;
import jakarta.inject.Inject;
import org.acme.redis.streams.aggregator.model.Temperature;
import org.acme.redis.streams.aggregator.model.TemperatureAggregate;
import org.acme.redis.streams.aggregator.model.WeatherStation;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.acme.redis.streams.aggregator.utils.Constants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AggregatorResourceTest {

    private static List<WeatherStation> WEATHER_STATIONS = List.of(
            new WeatherStation(1L, "Athens", 7.00),
            new WeatherStation(2L, "London", 3.00),
            new WeatherStation(3L, "Berlin", 15.00),
            new WeatherStation(4L, "Helsinki", 5.00),
            new WeatherStation(5L, "Munich", 12.00));

    private ValueCommands<String, WeatherStation> values;
    private KeyCommands<String> keys;
    private StreamCommands<String, String, Temperature> stream;

    @Inject
    RedisDataSource dataSource;

    @BeforeEach
    void setUp() {

        this.values = dataSource.value(String.class, WeatherStation.class);
        this.keys = dataSource.key(String.class);
        this.stream = dataSource.stream(Temperature.class);

        WEATHER_STATIONS.forEach(
                station -> values.set(STATION_TABLE + station.id, station));
    }

    @AfterEach
    void tearDown() {
        this.keys.del(
                WEATHER_STATIONS.stream()
                        .map( station -> String.valueOf(station.id)).toArray(String[]::new));

    }

    @Test
    void getTemperatureAggregatePerStation() {

        // arrange

        var args = new XAddArgs().maxlen(1_000L);
        for (int index = 0; index < 5; index++) {
            var data = new Temperature();
            data.id = 1L;
            data.temperature = 2.00;
            data.date = Instant.now().toString();

            this.stream.xadd(TEMPERATURE_VALUES_STREAM, args, Map.of(TEMPERATURE_VALUES_KEY, data));
        }

        // act && assert
        var result = given().when()
                .log().all()
                .accept(ContentType.JSON)
                .get("/temperatures/1")
                .then()
                .statusCode(200)
                .log()
                .ifValidationFails()
                .extract()
                .as(TemperatureAggregate.class);

        assertAll("Temperature Aggregates For station#1",
                () -> assertEquals(2.0, result.avg),
                () -> assertEquals(8.0, result.sum));

    }

    @Test
    void getWeatherStationByIdr() {

        // act
        var result = this.values.get("station#1");

        //assert
        assertAll(() -> assertEquals("Athens", result.name));
    }

}
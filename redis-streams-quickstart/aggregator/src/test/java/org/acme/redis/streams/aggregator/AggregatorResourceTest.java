package org.acme.redis.streams.aggregator;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.stream.StreamCommands;
import io.quarkus.redis.datasource.stream.XAddArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.quarkus.redis.runtime.datasource.BlockingRedisDataSourceImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.redis.client.Redis;
import io.vertx.mutiny.redis.client.RedisAPI;
import org.acme.redis.streams.aggregator.model.Temperature;
import org.acme.redis.streams.aggregator.model.TemperatureAggregate;
import org.acme.redis.streams.aggregator.model.WeatherStation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.acme.redis.streams.aggregator.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AggregatorResourceTest {

    private static List<WeatherStation> WEATHER_STATIONS = List.of(
            new WeatherStation(1L, "Athens", 7.00),
            new WeatherStation(2L, "London", 3.00),
            new WeatherStation(3L, "Berlin", 15.00),
            new WeatherStation(4L, "Helsinki", 5.00),
            new WeatherStation(5L, "Munich", 12.00));

     private static RedisDataSource dataSource;

    static Redis redis;
    static RedisAPI api;
    static Vertx vertx;

    private ValueCommands<String, WeatherStation> values;
    private KeyCommands<String> keys;
    private StreamCommands<String, String, Temperature> stream;

    @BeforeAll
    static void beforeAll() {
        vertx = Vertx.vertx();
        redis = Redis.createClient(vertx,
                "redis://localhost:6379");
        api = RedisAPI.api(redis);

        dataSource = new BlockingRedisDataSourceImpl(vertx, redis, api, Duration.ofSeconds(1));
    }



    @BeforeEach
    void setUp() {

        values = dataSource.value(WeatherStation.class);
        keys = dataSource.key(String.class);
        stream = dataSource.stream(Temperature.class);

        // populate weather stations
        WEATHER_STATIONS.forEach( station -> values.set(STATION_TABLE + station.id, station));
    }

    @AfterEach
    void tearDown() {
        this.keys.del(
                WEATHER_STATIONS.stream()
                        .map( station -> String.valueOf(station.id)).toArray(String[]::new));

    }

    @Test
    void getTemperatureAggregatePerStation() throws InterruptedException {

        // arrange
        var args = new XAddArgs().maxlen(1_000L);

        for (int index = 0; index < 5; index++) {
            var data = new Temperature(1L, 1.00 + index, Instant.now().toString());
            this.stream.xadd(TEMPERATURE_VALUES_STREAM, args, Map.of(TEMPERATURE_VALUES_KEY, data));

            // add some delay before each event ..
            Thread.sleep(1_000);
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
                () -> assertEquals("Athens", result.name),
                () -> assertEquals(3.0, result.avg),
                () -> assertEquals(15.0, result.sum),
                () -> assertEquals(5.00, result.max),
                () -> assertEquals(1.00, result.min));
    }

    @Test
    void getWeatherStationByIdr() {

        // act
        var result = this.values.get("station#1");

        //assert
        assertAll(() -> assertEquals("Athens", result.name));
    }

}
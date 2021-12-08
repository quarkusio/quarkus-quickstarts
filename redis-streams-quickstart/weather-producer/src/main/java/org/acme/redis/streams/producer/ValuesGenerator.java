package org.acme.redis.streams.producer;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.String.format;

@ApplicationScoped
public class ValuesGenerator {

    private static final String TEMPERATURE_VALUES_STREAM = "temperature-values";
    private static final String STATION_TABLE = "station:";
    private static final Logger log = LoggerFactory.getLogger(ValuesGenerator.class);

    private Random random = new Random();
    private List<WeatherStation> stations = Arrays.asList(
            new WeatherStation(1, "Hamburg", 13),
            new WeatherStation(2, "Snowdonia", 5),
            new WeatherStation(3, "Boston", 11),
            new WeatherStation(4, "Tokio", 16),
            new WeatherStation(5, "Cusco", 12),
            new WeatherStation(6, "Svalbard", -7),
            new WeatherStation(7, "Porthsmouth", 11),
            new WeatherStation(8, "Oslo", 7),
            new WeatherStation(9, "Marrakesh", 20));

    @ConfigProperty(name = "producer.rate", defaultValue = "1000")
    int rate;

    @Inject
    ReactiveRedisClient client;

    public void generateData(@Observes StartupEvent event) {
        Multi.createBy().concatenating().streams(generateStations(), generateTemperatures())
                .subscribe()
                .with(item -> log.debug("Added item: {}", item), err -> log.error("Caught exception: {}", err));
    }

    private Multi<Void> generateStations() {
        return Multi.createFrom().iterable(this.stations)
                .invoke(station -> log.debug("creating station: {}", station))
                .call(station -> this.client.set(Arrays.asList(STATION_TABLE + station.id, Json.encode(station))))
                .onItem().transformToUniAndMerge(res -> Uni.createFrom().voidItem());
    }

    private Multi<Void> generateTemperatures() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(this.rate))
                .onItem().transform(tick -> {
                    WeatherStation station = this.stations.get(this.random.nextInt(this.stations.size()));
                    double temperature = BigDecimal.valueOf(this.random.nextGaussian() * 15 + station.averageTemperature)
                            .setScale(1, RoundingMode.HALF_UP)
                            .doubleValue();

                    JsonObject stationTemp = new JsonObject();
                    return stationTemp.put("id", station.id)
                            .put("temperature", temperature)
                            .put("date", Instant.now().toString());
                })
                .onItem().transformToUniAndMerge(temp ->
                        this.client.xadd(Arrays.asList(TEMPERATURE_VALUES_STREAM,
                                "maxlen",
                                "~",
                                "100",
                                "*",
                                "payload", Json.encode(temp)))
                                .invoke(res -> log.debug("Added Message(id={}, payload={})", res.toString(), temp)))
                .onItem().transformToUniAndMerge(res -> Uni.createFrom().voidItem());
    }

    private static class WeatherStation {
        public int id;
        public String name;
        public int averageTemperature;

        public WeatherStation(int id, String name, int averageTemperature) {
            this.id = id;
            this.name = name;
            this.averageTemperature = averageTemperature;
        }

        @Override
        public String toString() {
            return format("WeatherStation(id=%s, name=%s, avgTemp=%d)", this.id, this.name, this.averageTemperature);
        }
    }
}

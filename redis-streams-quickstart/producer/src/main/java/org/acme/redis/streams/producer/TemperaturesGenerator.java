package org.acme.redis.streams.producer;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.stream.ReactiveStreamCommands;
import io.quarkus.redis.datasource.stream.XAddArgs;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.acme.redis.streams.producer.model.Temperature;
import org.acme.redis.streams.producer.model.WeatherStation;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static java.math.RoundingMode.HALF_UP;

@ApplicationScoped
public class TemperaturesGenerator {
    private static final Logger LOG = Logger.getLogger(TemperaturesGenerator.class);
    private static final String TEMPERATURE_VALUES_STREAM = "temperature-values";
    private static final String TEMPERATURE_VALUES_KEY = "temperature";
    private static final String STATION_TABLE = "station#";
    public static final Long MAX_LEN = 1_000L;

    @ConfigProperty(name = "producer.rate", defaultValue = "1000")
    int rate;

    private List<WeatherStation> stations = List.of(
            new WeatherStation(1L, "Athens", 13),
            new WeatherStation(2L, "Helsinki", -10),
            new WeatherStation(3L, "Boston", 11),
            new WeatherStation(4L, "Tokio", 16),
            new WeatherStation(5L, "Berlin", 12));

    ReactiveStreamCommands<String, String, String> stream;

    ReactiveValueCommands<String, String> values;

    public TemperaturesGenerator(ReactiveRedisDataSource dataSource) {
        this.stream = dataSource.stream(String.class);
        this.values = dataSource.value(String.class);
    }

    public void generateData(@Observes StartupEvent event) {
        Multi.createBy()
                .concatenating()
                .streams(generateStations(), generateTemperatures())
                .subscribe()
                .with(item -> LOG.infov("Added item: {0}", item),
                        err -> LOG.errorv("Caught exception: {0}", err));
    }

    private Multi<Void> generateStations() {
        return Multi.createFrom().iterable(this.stations)
                .invoke(station -> LOG.debugv("creating station: {0}", station))
                .call(station -> this.values.set(STATION_TABLE + station.id, Json.encode(station)))
                .onItem().transformToUniAndMerge(res -> Uni.createFrom().voidItem());
    }

    /*
     *  Generates a random temperature for a random station,
     * and sends this to the "temperatures-values" stream as JSON payload.
     */
    private Multi<Void> generateTemperatures() {
        return Multi.createFrom()

                .ticks().every(Duration.ofMillis(this.rate))

                .onItem().transform(tick -> {

                    var station = this.stations.get(ThreadLocalRandom.current().nextInt(this.stations.size()));

                    // calculate a random value
                    double temperature = BigDecimal
                            .valueOf(ThreadLocalRandom.current().nextGaussian() * 15 + station.averageTemperature)
                            .setScale(1, HALF_UP)
                            .doubleValue();

                    return new Temperature(station.id, temperature, Instant.now().toString());

                })

                .onItem().transformToUniAndMerge(temp -> {
                    var args = new XAddArgs().maxlen(MAX_LEN);

                    LOG.debugv("Payload: {0}", Json.encode(temp));

                    return this.stream.xadd(TEMPERATURE_VALUES_STREAM, args,
                                    Map.of(TEMPERATURE_VALUES_KEY, Json.encode(temp)))
                            .invoke(res ->
                                    LOG.infov("Added Message(id={0}, payload={1})", res, temp));
                })
                .onItem().transformToUniAndMerge(res -> Uni.createFrom().voidItem());
    }
}

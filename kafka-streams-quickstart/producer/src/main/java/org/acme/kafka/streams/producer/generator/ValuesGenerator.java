package org.acme.kafka.streams.producer.generator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;

/**
 * A bean producing random temperature data every second.
 * The values are written to a Kafka topic (temperature-values).
 * Another topic contains the name of weather stations (weather-stations).
 * The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class ValuesGenerator {

    private static final Logger LOG = Logger.getLogger(ValuesGenerator.class);

    private Random random = new Random();

    private List<WeatherStation> stations = Collections.unmodifiableList(
            Arrays.asList(
                    new WeatherStation(1, "Hamburg", 13),
                    new WeatherStation(2, "Snowdonia", 5),
                    new WeatherStation(3, "Boston", 11),
                    new WeatherStation(4, "Tokio", 16),
                    new WeatherStation(5, "Cusco", 12),
                    new WeatherStation(6, "Svalbard", -7),
                    new WeatherStation(7, "Porthsmouth", 11),
                    new WeatherStation(8, "Oslo", 7),
                    new WeatherStation(9, "Marrakesh", 20)));

    @Outgoing("temperature-values")
    public Flowable<KafkaRecord<Integer, String>> generate() {

        return Flowable.interval(500, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .map(tick -> {
                    WeatherStation station = stations.get(random.nextInt(stations.size()));
                    double temperature = BigDecimal.valueOf(random.nextGaussian() * 15 + station.averageTemperature)
                            .setScale(1, RoundingMode.HALF_UP)
                            .doubleValue();

                    LOG.infov("station: {0}, temperature: {1}", station.name, temperature);
                    return KafkaRecord.of(station.id, Instant.now() + ";" + temperature);
                });
    }

    @Outgoing("weather-stations")
    public Flowable<KafkaRecord<Integer, String>> weatherStations() {
        List<KafkaRecord<Integer, String>> stationsAsJson = stations.stream()
                .map(s -> KafkaRecord.of(s.id, "{ \"id\" : " + s.id + ", \"name\" : \"" + s.name + "\" }"))
                .collect(Collectors.toList());

        return Flowable.fromIterable(stationsAsJson);
    };

    private static class WeatherStation {

        int id;
        String name;
        int averageTemperature;

        public WeatherStation(int id, String name, int averageTemperature) {
            this.id = id;
            this.name = name;
            this.averageTemperature = averageTemperature;
        }
    }
}

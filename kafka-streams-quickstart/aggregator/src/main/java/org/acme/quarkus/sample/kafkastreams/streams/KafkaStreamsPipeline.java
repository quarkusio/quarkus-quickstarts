package org.acme.quarkus.sample.kafkastreams.streams;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.acme.quarkus.sample.kafkastreams.model.Aggregation;
import org.acme.quarkus.sample.kafkastreams.model.TemperatureMeasurement;
import org.acme.quarkus.sample.kafkastreams.model.WeatherStation;
import org.acme.quarkus.sample.kafkastreams.model.WeatherStationData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.StreamsMetadata;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.kafka.client.serialization.JsonbSerde;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class KafkaStreamsPipeline {

    private static final String WEATHER_STATIONS_STORE = "weather-stations-store";

    private static final String WEATHER_STATIONS_TOPIC = "weather-stations";
    private static final String TEMPERATURE_VALUES_TOPIC = "temperature-values";
    private static final String TEMPERATURES_AGGREGATED_TOPIC = "temperatures-aggregated";

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsPipeline.class);

    @ConfigProperty(name="org.acme.quarkus.sample.kafkastreams.bootstrap.servers")
    String bootstrapServers;

    @ConfigProperty(name="HOSTNAME")
    String host;

    @ConfigProperty(name = "quarkus.http.port")
    int port;

    private KafkaStreams streams;

    private ExecutorService executor;

    void onStart(@Observes StartupEvent ev) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "temperature-aggregator");
        props.put(StreamsConfig.APPLICATION_SERVER_CONFIG, host + ":" + port);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 10 * 1024);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        props.put(CommonClientConfigs.METADATA_MAX_AGE_CONFIG, 500);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        StreamsBuilder builder = new StreamsBuilder();

        JsonbSerde<WeatherStation> weatherStationSerde = new JsonbSerde<>(WeatherStation.class);
        JsonbSerde<Aggregation> aggregationSerde = new JsonbSerde<>(Aggregation.class);

        KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore(WEATHER_STATIONS_STORE);

        GlobalKTable<Integer, WeatherStation> stations = builder.globalTable(
                WEATHER_STATIONS_TOPIC,
                Consumed.with(Serdes.Integer(), weatherStationSerde));

        builder.stream(
                        TEMPERATURE_VALUES_TOPIC,
                        Consumed.with(Serdes.Integer(), Serdes.String())
                )
                .join(
                        stations,
                        (stationId, timestampAndValue) -> stationId,
                        (timestampAndValue, station) -> {
                            String[] parts = timestampAndValue.split(";");
                            return new TemperatureMeasurement(station.id, station.name, Instant.parse(parts[0]), Double.valueOf(parts[1]));
                        }
                )
                .groupByKey()
                .aggregate(
                        Aggregation::new,
                        (stationId, value, aggregation) -> aggregation.updateFrom(value),
                        Materialized.<Integer, Aggregation> as(storeSupplier)
                            .withKeySerde(Serdes.Integer())
                            .withValueSerde(aggregationSerde)
                )
                .toStream()
                .to(
                        TEMPERATURES_AGGREGATED_TOPIC,
                        Produced.with(Serdes.Integer(), aggregationSerde)
                );

        streams = new KafkaStreams(builder.build(), props);

        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            waitForTopicsToBeCreated(bootstrapServers);
            streams.start();
        });
    }

    void onStop(@Observes ShutdownEvent ev) {
        streams.close();
        executor.shutdown();
    }

    public List<PipelineMetadata> getMetaData() {
        return streams.allMetadataForStore(WEATHER_STATIONS_STORE)
                .stream()
                .map(m -> new PipelineMetadata(
                        m.hostInfo().host() + ":" + m.hostInfo().port(),
                        m.topicPartitions()
                            .stream()
                            .map(TopicPartition::toString)
                            .collect(Collectors.toSet()))
                )
                .collect(Collectors.toList());
    }

    public GetWeatherStationDataResult getWeatherStationData(int id) {
        StreamsMetadata metadata = streams.metadataForKey(
                WEATHER_STATIONS_STORE,
                id,
                Serdes.Integer().serializer()
        );

        if (metadata == null || metadata == StreamsMetadata.NOT_AVAILABLE) {
            LOG.warn("Found no metadata for key {}", id);
            return GetWeatherStationDataResult.notFound();
        }
        else if (metadata.host().equals(host)) {
            LOG.info("Found data for key {} locally", id);
            Aggregation result = getWeatherStationStore().get(id);

            if (result != null) {
                return GetWeatherStationDataResult.found(WeatherStationData.from(result));
            }
            else {
                return GetWeatherStationDataResult.notFound();
            }
        }
        else {
            LOG.info("Found data for key {} on remote host {}:{}", id, metadata.host(), metadata.port());
            return GetWeatherStationDataResult.foundRemotely(metadata.host(), metadata.port());
        }
    }

    private ReadOnlyKeyValueStore<Integer, Aggregation> getWeatherStationStore() {
        while (true) {
            try {
                return streams.store(WEATHER_STATIONS_STORE, QueryableStoreTypes.keyValueStore());
            } catch (InvalidStateStoreException e) {
                // ignore, store not ready yet
            }
        }
    }

    private void waitForTopicsToBeCreated(String bootstrapServers) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(config)) {
            while (true) {
                ListTopicsResult topics = adminClient.listTopics();
                Set<String> topicNames = topics.names().get(60, TimeUnit.SECONDS);

                if (topicNames.contains(WEATHER_STATIONS_TOPIC) && topicNames.contains(TEMPERATURE_VALUES_TOPIC)) {
                    return;
                }

                LOG.info("Waiting for topics {} and {} to be created", WEATHER_STATIONS_TOPIC, TEMPERATURE_VALUES_TOPIC);
                Thread.sleep(1_000);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}

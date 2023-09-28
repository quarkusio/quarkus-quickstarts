package org.acme.redis.streams.aggregator.processors;

import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.quarkus.redis.datasource.stream.*;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.MultiCreateBy;
import io.smallrye.mutiny.subscription.Cancellable;
import io.vertx.core.json.Json;
import io.vertx.redis.client.Response;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.redis.streams.aggregator.exceptions.NotFoundException;
import org.acme.redis.streams.aggregator.model.Temperature;
import org.acme.redis.streams.aggregator.model.TemperatureAggregate;
import org.acme.redis.streams.aggregator.model.WeatherStation;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.*;
import static org.acme.redis.streams.aggregator.utils.Constants.*;

@Startup
@ApplicationScoped
public class TemperatureProcessor {

    private static final Logger LOG = Logger.getLogger(TemperatureProcessor.class);

    private ReactiveStreamCommands<String, String, String> stream;
    private ReactiveHashCommands<String, String, String> hash;
    private ReactiveValueCommands<String, String> keys;

    private Cancellable consumer;

    public TemperatureProcessor(ReactiveRedisDataSource dataSource) {
        this.stream = dataSource.stream(String.class);
        this.hash = dataSource.hash(String.class);
        this.keys = dataSource.value(String.class);
    }

    @PostConstruct
    void onStart() {
        LOG.info("Stream Aggregator listener is starting...");

        var args = new XGroupCreateArgs().mkstream();

        this.consumer = this.stream
                .xgroupCreate(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP, "0-0", args)
                .onFailure()
                .recoverWithNull()
                .map(v -> this.stream.xgroupCreateConsumer(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP, CONSUMER_NAME))
                .map(v -> createStreamListener())
                .subscribe()
                .with(cancellable -> this.consumer = cancellable);

        LOG.infov("Consumer {0}, subscribed to {1} stream with group {2}",
                CONSUMER_NAME, TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP);
    }

    @PreDestroy
    void onStop() {
        LOG.info("The application is stopping...");
        this.consumer.cancel();

        this.stream
                .xgroupDelConsumer(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP, CONSUMER_NAME)
                .subscribe()
                .with(unack -> LOG.infov("Consumer deleted with {0} unacknowledged messages", unack));
    }

    private Cancellable createStreamListener() {

        XReadGroupArgs args = new XReadGroupArgs()
                .noack()
                .block(Duration.ofSeconds(60))
                .count(1_000);

        return Multi.createBy()
                .repeating()
                .uni(() -> this.stream
                        .xreadgroup(CONSUMER_GROUP, CONSUMER_NAME, TEMPERATURE_VALUES_STREAM, ">", args)
                        .onFailure()
                        .recoverWithItem(error -> {
                            LOG.errorv("An error occurred during reading from stream with error: {0}", error);
                            return emptyList();
                        }))
                .indefinitely()
                .map(this::processMessage)
                .onFailure()
                .recoverWithItem(e -> {
                    LOG.errorv("Cannot process message with error {0}", e);
                    return new TemperatureAggregate();
                })
                .onItem()
                .transformToUniAndMerge(entry ->
                        this.hash.hget(AGGREGATE_TABLE, entry.stationId.toString()).map(oldAgg -> {
                            TemperatureAggregate old = getTemperatureAggregate(oldAgg);
                            return   entry.calculate(old);
                        })
                )
                .call(agg -> this.keys.get(STATION_TABLE + agg.stationId.toString())
                        .invoke(station -> setWeatherStationName(agg, station)))
                .onFailure().invoke(err -> LOG.errorv("Caught exception: {0}", err))
                .call(agg -> this.hash.hset(AGGREGATE_TABLE, agg.stationId.toString(), Json.encode(agg)))
                .onFailure().invoke(err -> LOG.errorv("Caught exception: {0}", err))
                .call(agg -> this.stream.xack(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP, agg.messageIds.toArray(String[]::new)))
                .onFailure().invoke(err -> LOG.errorv("Caught exception: {0}", err))
                .subscribe()
                .with(count -> LOG.infov("Collected {0} requests", count));
    }

    private TemperatureAggregate processMessage(List<StreamMessage<String, String, String>> messages) {
        return messages.stream()
                .findFirst()
                .map(entry -> {
                    Temperature temperature = Json.decodeValue(entry.payload().get("temperature"), Temperature.class);
                    temperature.messageId = entry.id();
                    var agg = new TemperatureAggregate(temperature);
                    return agg.calculate(temperature);
                })
                .get();
    }

    private TemperatureAggregate setWeatherStationName(TemperatureAggregate agg, String response) {

        LOG.infov("Retrieved Station: {0}", response);
        if (isJson(response)) {
            WeatherStation station = Json.decodeValue(response, WeatherStation.class);
            agg.name = station.name;
        }
        return agg;
    }
    private boolean isJson(String res) {
        return res != null && res.startsWith("{");
    }

    private TemperatureAggregate getTemperatureAggregate(String res) {
        TemperatureAggregate agg;
        if (isJson(res)) {
            agg = Json.decodeValue(res.toString(), TemperatureAggregate.class);
        } else {
            agg = new TemperatureAggregate();
        }
        return agg;
    }
    Uni<Integer> acknowledge(String[] ids) {
        LOG.infov("Acknowledge {0} messages and ids: {1}", ids.length );

        return ids.length > 0 ? this.stream.xack(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP, ids) : Uni.createFrom().item(0);
    }

    @Scheduled(every = "1h")
    void trimStream() {
        var args = new XTrimArgs().maxlen(10000).nearlyExactTrimming();
        this.stream.xtrim(TEMPERATURE_VALUES_STREAM, args)
                .subscribe()
                .with(count -> LOG.infov("Trimmed {0} messages from stream {1}", count, TEMPERATURE_VALUES_STREAM));
    }

    public Uni<String> getTemperatureAggregateByStationId(String id) {
        LOG.infov("Retrieve temperatures aggregates for station: {0}", id);

        return this.hash.hget(AGGREGATE_TABLE, id)
                .onItem()
                .ifNotNull()
                .transform(res -> res)
                .onItem()
                .ifNull()
                .failWith(new NotFoundException("TemperatureAggregate(id=%s) not found", id));
    }
}

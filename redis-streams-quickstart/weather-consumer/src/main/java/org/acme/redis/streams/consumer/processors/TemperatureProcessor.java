package org.acme.redis.streams.consumer.processors;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import io.vertx.mutiny.redis.client.Response;
import io.vertx.redis.client.ResponseType;
import org.acme.redis.streams.consumer.domain.Temperature;
import org.acme.redis.streams.consumer.domain.TemperatureAggregate;
import org.acme.redis.streams.consumer.domain.WeatherStation;
import org.acme.redis.streams.consumer.exceptions.ApplicationException;
import org.acme.redis.streams.consumer.exceptions.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.acme.redis.streams.consumer.util.AppConstants.AGGREGATE_TABLE;
import static org.acme.redis.streams.consumer.util.AppConstants.CONSUMER_GROUP;
import static org.acme.redis.streams.consumer.util.AppConstants.TEMPERATURE_VALUES_STREAM;
import static org.acme.redis.streams.consumer.util.AppConstants.WEATHER_STATIONS_TABLE;
import static org.acme.redis.streams.consumer.util.RedisSupport.toHSetCommand;
import static org.acme.redis.streams.consumer.util.RedisSupport.toXAckCommand;
import static org.acme.redis.streams.consumer.util.RedisSupport.toXReadGroupCommand;

/**
 * <p>
 * Calculates in a reactive way the following temperature statistics from the 'temperature-values' stream:
 * <ul>
 *     <li>max</li>
 *     <li>min</li>
 *     <li>avg</li>
 *     <li>count</li>
 * </ul>
 *  </p>
 *
 *  <p>
 *  And stores this as a Json encoded string in a Redis Hash keystore.
 *  </p>
 *
 *  <p>
 *      Example data returned from the stream: <br/>
 *      <quote>
 *           1) "temperature-values"
 *           2) 1) 1) "1603280570706-0"
 *                 2) 1) "payload"
 *                    2) "{\"id\":4,\"temperature\":18.1,\"date\":\"2020-10-21T11:42:50.689678Z\"}"
 *              2) 1) "1603280571690-0"
 *                 2) 1) "payload"
 *                    2) "{\"id\":5,\"temperature\":12.4,\"date\":\"2020-10-21T11:42:51.688515Z\"}"<br/>
 *      </quote>
 *  </p>
 *
 * @author Serkan Eskici
 */
@ApplicationScoped
public class TemperatureProcessor {

    private static final Logger log = LoggerFactory.getLogger(TemperatureProcessor.class);

    @ConfigProperty(name = "quarkus.application.name")
    String appName;

    @Inject
    ReactiveRedisClient client;

    /**
     * Processes the stream and calculates temperature aggregates and stores them in Redis (in a Hash keystore).
     *
     * @param consumerGroup
     * @return a stream/multi of processed temperature aggregates.
     */
    public Multi<TemperatureAggregate> calculateAggregates(String consumerGroup) {

        // read the latest temperatures/messages from the stream
        return this.client.xreadgroup(toXReadGroupCommand(consumerGroup, this.appName, TEMPERATURE_VALUES_STREAM))
                 // note: you retrieve a Multi (=list) from the Stream ! Most of the time it will be 1 message, but when the load is high on the stream
                 // you can get multiple messages at the same time ! So we need to iterate through them.
                .onItem().ifNotNull().transformToMulti(res -> {
                    if (ResponseType.MULTI.equals(res.type()) && res.get(0) != null && res.get(0).size() >= 2) {
                        return Multi.createFrom().iterable(res.get(0).get(1));
                    }
                    throw new ApplicationException("Wrong payload in stream: %s", res.toString());
                })
                .filter(msg -> msg.get(1) != null)
                // map each message into temperature
                .map(this::toTemperature)
                // save incoming temperatures into a map(key=stationId, value=TemperatureAggregate) by aggregating them with .calculate(..)
                .collectItems().in(() -> new HashMap<Long, TemperatureAggregate>(), (map, temperature) -> {                    
                    map.computeIfPresent(temperature.id, (key, value) -> value.calculate(temperature));
                    map.computeIfAbsent(temperature.id, key -> new TemperatureAggregate(temperature));
                })
                // loop over the aggregated map
                .onItem().transformToMulti(map -> Multi.createFrom().iterable(map.entrySet()))
                // for each entry => retrieve the old agg from Redis, if (old) agg already exists, add the current agg with the old one.
                .onItem().transformToUniAndMerge(entry ->
                        this.client.hget(AGGREGATE_TABLE, entry.getKey().toString()).map(oldAgg -> {
                            TemperatureAggregate old = getTemperatureAggregate(oldAgg);
                            return entry.getValue().calculate(old);
                        })
                )
                // get the weatherstation belonging to this temperature from Redis, and set its name into the agg
                .call(agg -> this.client.get(WEATHER_STATIONS_TABLE + agg.stationId).invoke(station -> setWeatherStationName(agg, station)))
                // save the agg into a hash store
                .call(agg -> this.client.hset(toHSetCommand(AGGREGATE_TABLE, agg.stationId.toString(), agg)))
                // acknowledge the messages to the stream
                .call(agg -> this.client.xack(toXAckCommand(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP, new ArrayList<>(agg.messageIds))))
                .onFailure().invoke(err -> log.error("Caught exception: {}", err));
    }

    /**
     * @return the temperature aggregates stored in Redis (in a Hash keystore).
     */
    public Uni<Collection<String>> getTemperatureAggregates() {
        // hgetall will return:
        // [1] (=stationId)
        // {json string} (=aggregate in json form)
        // [2]
        // {json string}
        // etc.
        return this.client.hgetall(AGGREGATE_TABLE)
                // iterate through the hash and create a pair of <stationId, json>
                .onItem().ifNotNull().transformToMulti(res -> Multi.createBy().repeating().uni(
                        AtomicInteger::new,
                        i -> Uni.createFrom().item(() -> {
                            if (i.get() % 2 != 0) {
                                i.incrementAndGet();
                            }

                            // even numbers in the response are the keys, uneven numbers are the values
                            return Collections.singletonMap(res.get(i.get()).toString(), res.get(i.incrementAndGet()).toString());
                        }))
                        .atMost(res.size() / 2))
                .flatMap(map -> Multi.createFrom().iterable(map.entrySet()))
                // save each pair in a sorted map, so that we get the results sorted by stationId
                .collectItems().in(() -> new TreeMap<String, String>(), (map, entry) -> map.put(entry.getKey(), entry.getValue()))
                .map(map -> map.values())
                .onFailure().recoverWithItem(Collections.emptyList());
    }

    /**
     * Returns the aggregate by stationId stored in Redis in a Hash keystore.
     *
     * @param id the station id.
     * @return the aggregate.
     */
    public Uni<String> getTemperatureAggregateByStationId(String id) {
        return this.client.hget(AGGREGATE_TABLE, id)
                .onItem().ifNotNull().transform(res -> res.toString())
                .onItem().ifNull().failWith(new NotFoundException("TemperatureAggregate(id=%s) not found", id));
    }

    private TemperatureAggregate getTemperatureAggregate(Response res) {
        TemperatureAggregate agg;
        if (isJson(res)) {
            agg = Json.decodeValue(res.toString(), TemperatureAggregate.class);
        } else {
            agg = new TemperatureAggregate();
        }
        return agg;
    }

    private Temperature toTemperature(Response msg) {
        if (msg != null && msg.size() >= 2) {
            if (msg.get(1) != null) {
                Response json = msg.get(1).get(1);
                if (isJson(json)) {
                    String msgId = msg.get(0).toString();
                    String payload = json.toString();
                    Temperature temperature = Json.decodeValue(payload, Temperature.class);
                    temperature.messageId = msgId;
                    return temperature;
                }
            }
        }
        throw new ApplicationException("Could not find the correct temperature payload in the message: %s", msg.toString());
    }

    private TemperatureAggregate setWeatherStationName(TemperatureAggregate agg, Response response) {
        if (isJson(response)) {
            WeatherStation station = Json.decodeValue(response.toString(), WeatherStation.class);
            agg.name = station.name;
        }
        return agg;
    }

    private boolean isJson(Response res) {
        return res != null && res.toString().startsWith("{");
    }
}

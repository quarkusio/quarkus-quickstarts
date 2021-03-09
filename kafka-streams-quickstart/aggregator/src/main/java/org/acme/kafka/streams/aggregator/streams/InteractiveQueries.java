package org.acme.kafka.streams.aggregator.streams;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.kafka.streams.aggregator.model.Aggregation;
import org.acme.kafka.streams.aggregator.model.WeatherStationData;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyQueryMetadata;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.StreamsMetadata;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InteractiveQueries {

    private static final Logger LOG = Logger.getLogger(InteractiveQueries.class);

    @ConfigProperty(name = "hostname")
    String host;

    @Inject
    KafkaStreams streams;

    public List<PipelineMetadata> getMetaData() {
        return streams.allMetadataForStore(TopologyProducer.WEATHER_STATIONS_STORE)
                .stream()
                .map(m -> new PipelineMetadata(
                        m.hostInfo().host() + ":" + m.hostInfo().port(),
                        m.topicPartitions()
                                .stream()
                                .map(TopicPartition::toString)
                                .collect(Collectors.toSet())))
                .collect(Collectors.toList());
    }

    public GetWeatherStationDataResult getWeatherStationData(int id) {
        KeyQueryMetadata metadata = streams.queryMetadataForKey(
//        StreamsMetadata metadata = streams.metadataForKey(
                TopologyProducer.WEATHER_STATIONS_STORE,
                id,
                Serdes.Integer().serializer());


        if (metadata == null || metadata == KeyQueryMetadata.NOT_AVAILABLE) {
//        if (metadata == null || metadata == StreamsMetadata.NOT_AVAILABLE) {
            LOG.warnv("Found no metadata for key {0}", id);
            return GetWeatherStationDataResult.notFound();
//        } else if (metadata.host().equals(host)) {
        } else if (metadata.getActiveHost().host().equals(host)) {
            LOG.infov("Found data for key {0} locally", id);
            Aggregation result = getWeatherStationStore().get(id);

            if (result != null) {
                return GetWeatherStationDataResult.found(WeatherStationData.from(result));
            } else {
                return GetWeatherStationDataResult.notFound();
            }
        } else {
            LOG.infov("Found data for key {0} on remote host {1}:{2}", id,
                    metadata.getActiveHost().host(),
                    metadata.getActiveHost().port());
            return GetWeatherStationDataResult.foundRemotely(
                    metadata.getActiveHost().host(),
                    metadata.getActiveHost().port());
        }
    }

    private ReadOnlyKeyValueStore<Integer, Aggregation> getWeatherStationStore() {
        while (true) {
            try {
                StoreQueryParameters<ReadOnlyKeyValueStore<Integer, Aggregation>> params =
                        StoreQueryParameters
                                .fromNameAndType(
                                        TopologyProducer.WEATHER_STATIONS_STORE,
                                        QueryableStoreTypes.keyValueStore());

                return streams.store(params);
            } catch (InvalidStateStoreException e) {
                // ignore, store not ready yet
            }
        }
    }
}

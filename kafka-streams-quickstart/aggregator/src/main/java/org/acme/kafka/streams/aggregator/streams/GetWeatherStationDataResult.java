package org.acme.kafka.streams.aggregator.streams;

import java.util.Optional;
import java.util.OptionalInt;

import org.acme.kafka.streams.aggregator.model.WeatherStationData;

public class GetWeatherStationDataResult {

    private static GetWeatherStationDataResult NOT_FOUND = new GetWeatherStationDataResult(null, null, null);

    private final WeatherStationData result;
    private final String host;
    private final Integer port;

    private GetWeatherStationDataResult(WeatherStationData result, String host, Integer port) {
        this.result = result;
        this.host = host;
        this.port = port;
    }

    public static GetWeatherStationDataResult found(WeatherStationData data) {
        return new GetWeatherStationDataResult(data, null, null);
    }

    public static GetWeatherStationDataResult foundRemotely(String host, int port) {
        return new GetWeatherStationDataResult(null, host, port);
    }

    public static GetWeatherStationDataResult notFound() {
        return NOT_FOUND;
    }

    public Optional<WeatherStationData> getResult() {
        return Optional.ofNullable(result);
    }

    public Optional<String> getHost() {
        return Optional.ofNullable(host);
    }

    public OptionalInt getPort() {
        return port != null ? OptionalInt.of(port) : OptionalInt.empty();
    }
}

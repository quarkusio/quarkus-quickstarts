package org.acme.kafka.streams.aggregator.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class WeatherStationData {

    public int stationId;
    public String stationName;
    public double min = Double.MAX_VALUE;
    public double max = Double.MIN_VALUE;
    public int count;
    public double avg;

    private WeatherStationData(int stationId, String stationName, double min, double max, int count, double avg) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.min = min;
        this.max = max;
        this.count = count;
        this.avg = avg;
    }

    public static WeatherStationData from(Aggregation aggregation) {
        return new WeatherStationData(
                aggregation.stationId,
                aggregation.stationName,
                aggregation.min,
                aggregation.max,
                aggregation.count,
                aggregation.avg);
    }
}

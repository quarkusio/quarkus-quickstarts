package org.acme.quarkus.sample.kafkastreams.model;

public class TemperatureMeasurement {

    public int stationId;
    public String stationName;
    public double value;

    public TemperatureMeasurement() {
    }

    public TemperatureMeasurement(int stationId, String stationName, double value) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.value = value;
    }
}

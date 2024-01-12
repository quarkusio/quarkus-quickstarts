package org.acme.redis.streams.aggregator.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class WeatherStation {
    public Long id;
    public String name;
    public Double averageTemperature;

    public WeatherStation() {}

    public WeatherStation(Long id, String name, Double averageTemperature) {
        this.id = id;
        this.name = name;
        this.averageTemperature = averageTemperature;
    }

    @Override
    public String toString() {
        return "WeatherStation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", averageTemperature=" + averageTemperature +
                '}';
    }
}
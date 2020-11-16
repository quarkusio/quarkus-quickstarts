package org.acme.redis.streams.consumer.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class WeatherStation {
    public Long id;
    public String name;
    public Double averageTemperature;

    public WeatherStation() {
    }

    public WeatherStation(Long id, String name, Double averageTemperature) {
        this.id = id;
        this.name = name;
        this.averageTemperature = averageTemperature;
    }
}

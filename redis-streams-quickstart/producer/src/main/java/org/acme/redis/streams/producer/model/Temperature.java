package org.acme.redis.streams.producer.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import static java.lang.String.format;

@RegisterForReflection
public class Temperature {
    public Long id;
    public double temperature;
    public String date;

    public Temperature(Long id, double temperature, String date) {
        this.id = id;
        this.temperature = temperature;
        this.date = date;
    }

    @Override
    public String toString() {
        return format("Temperature(id=%d, temperature=%f, date=%s)",
                this.id,
                this.temperature,
                this.date);
    }
}
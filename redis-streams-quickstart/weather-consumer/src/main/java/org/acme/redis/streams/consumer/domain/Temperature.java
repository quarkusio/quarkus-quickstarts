package org.acme.redis.streams.consumer.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import static java.lang.String.format;

@RegisterForReflection
public class Temperature {
    public Long id;
    public String messageId;
    public double temperature;
    public String date;

    public Temperature() {
    }

    public Temperature(Long id, String messageId, double temperature, String date) {
        this.id = id;
        this.messageId = messageId;
        this.temperature = temperature;
        this.date = date;
    }

    @Override
    public String toString() {
        return format("Temperature(id=%d, msgId=%s, temp=%f, date=%s)",
                this.id,
                this.messageId,
                this.temperature,
                this.date);
    }
}

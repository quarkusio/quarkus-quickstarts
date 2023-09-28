package org.acme.redis.streams.aggregator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.format;

@RegisterForReflection
public class TemperatureAggregate {
    public Long stationId;
    public String name;
    public Double max;
    public Double min;
    public double avg;
    public double sum;
    public long count;

    @JsonIgnore
    public Set<String> messageIds = new HashSet<>();

    public TemperatureAggregate() {}

    public TemperatureAggregate(Temperature temperature) {
        this.stationId = temperature.id;
        this.max = temperature.temperature;
        this.min = temperature.temperature;
        this.avg = temperature.temperature;
        this.messageIds.add(temperature.messageId);
    }

    public TemperatureAggregate calculate(Temperature temperature) {
        this.stationId = temperature.id;
        this.messageIds.add(temperature.messageId);

        if (this.max == null) {
            this.max = temperature.temperature;
        }
        if (this.min == null) {
            this.min = temperature.temperature;
        }

        this.max = max(this.max, temperature.temperature);
        this.min = min(this.min, temperature.temperature);
        this.sum = round(this.sum + temperature.temperature, 0);
        this.count++;
        this.avg = round(this.sum / this.count, 1);

        return this;
    }

    public TemperatureAggregate calculate(TemperatureAggregate agg) {
        if (agg.max != null) {
            this.stationId = agg.stationId;
            this.messageIds.addAll(agg.messageIds);

            if (this.max == null) {
                this.max = agg.max;
            }
            if (this.min == null) {
                this.min = agg.min;
            }
            this.max = max(agg.max, this.max);
            this.min = min(agg.min, this.min);
            this.sum = round(this.sum + agg.sum, 0);
            this.count += agg.count;
            this.avg = round(this.sum / this.count, 1);
        }
        return this;
    }

    @Override
    public String toString() {
        return format("TemperatureAggregate(id=%s, name=%s, max=%.1f, min=%.1f, avg=%.1f, count=%d, sum=%.0f, msgIds=%s)",
                this.stationId,
                this.name,
                this.max,
                this.min,
                this.avg,
                this.count,
                this.sum,
                this.messageIds);
    }

    private static double round(double a, int rounding) {
        return BigDecimal.valueOf(a).setScale(rounding, RoundingMode.HALF_UP).doubleValue();
    }
}
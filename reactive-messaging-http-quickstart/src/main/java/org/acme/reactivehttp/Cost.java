package org.acme.reactivehttp;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Cost {
    private double value;
    private String currency;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

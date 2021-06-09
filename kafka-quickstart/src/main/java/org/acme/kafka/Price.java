package org.acme.kafka;

public class Price {

    public double value;
    public String currency;

    public Price() { }

    public Price(double value, String currency) {
        this.value = value;
        this.currency = currency;
    }
}

package org.acme.reactivews;

public class Cost {
    private double value;
    private String currency;

    public Cost(double value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public Cost() {
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }
}

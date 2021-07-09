package org.acme.kafka.model;

public class Quote {

    public String id;
    public int price;

    @Override
    public String toString() {
        return "Quote{" +
                "id='" + id + '\'' +
                ", price=" + price +
                '}';
    }
}

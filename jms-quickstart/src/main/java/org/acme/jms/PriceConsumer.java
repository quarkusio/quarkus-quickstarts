package org.acme.jms;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkiverse.jms.JmsListener;

@ApplicationScoped
public class PriceConsumer {

    private volatile String lastPrice;

    public String getLastPrice() {
        return lastPrice;
    }

    @JmsListener("prices")
    public void onPrice(String price) {
        System.out.println("Last price " + price);
        lastPrice = price;
    }
}

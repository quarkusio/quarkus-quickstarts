package org.acme.panache;

import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class PriceStorage {

    /**
     * Classic Hibernate is blocking (unlike Hibernate Reactive), so we need the @Blocking annotation.
     * @param priceInUsd  the price
     */
    @Incoming("prices")
    @Blocking
    @Transactional
    public void store(int priceInUsd) {
        Price price = new Price();
        price.value = priceInUsd;
        price.persist();
    }

}

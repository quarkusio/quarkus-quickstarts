package org.acme.reactivews;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

/**
 * A bean consuming costs in multiple currencies and producing prices in EUR from them
 */
@ApplicationScoped
public class CostConverter {

    private static final Map<String, Double> conversionRatios = new HashMap<>();

    static {
        conversionRatios.put("CHF", 0.93);
        conversionRatios.put("USD", 0.84);
        conversionRatios.put("PLN", 0.22);
        conversionRatios.put("EUR", 1.0);
    }

    @Incoming("incoming-costs")
    @Outgoing("outgoing-costs")
    Cost convert(Cost cost) {
        Double conversionRatio = conversionRatios.get(cost.getCurrency().toUpperCase());
        if (conversionRatio == null) {
            return cost;
        }
        return new Cost(conversionRatio * cost.getValue(), "EUR");
    }
}

package org.acme.reactivews;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/collected-costs")
@ApplicationScoped
public class CostCollector {
    private double sum;

    @GET
    public synchronized double getCosts() {
        return sum;
    }

    @Incoming("collector")
    synchronized void collect(Cost cost) {
        if ("EUR".equals(cost.getCurrency())) {
            sum += cost.getValue();
        }
    }
}

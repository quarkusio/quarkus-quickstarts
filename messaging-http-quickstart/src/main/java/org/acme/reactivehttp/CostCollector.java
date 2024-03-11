package org.acme.reactivehttp;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/cost-collector")
@ApplicationScoped
public class CostCollector {

    private double sum = 0;

    @POST
    public synchronized void consumeCost(String valueAsString) {
        sum += Double.parseDouble(valueAsString);
    }

    @GET
    public synchronized double getSum() {
        return sum;
    }

}

package org.acme.reactivehttp;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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

package org.acme.context;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/")
public class PriceResource {

    @Inject @Channel("prices") Emitter<Double> emitter;

    @POST
    public void postAPrice(Price price) {
        emitter.send(price.value);
    }

}

package org.acme.context;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/")
public class PriceResource {

    @Inject @Channel("prices")
    MutinyEmitter<Double> emitter;

    @POST
    public Uni<Void> postAPrice(Price price) {
        if (emitter.hasRequests()) {
            return emitter.send(price.value);
        } else {
            return Uni.createFrom().nullItem();
        }
    }

}

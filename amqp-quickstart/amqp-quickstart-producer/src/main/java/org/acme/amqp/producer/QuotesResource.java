package org.acme.amqp.producer;

import java.util.UUID;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.acme.amqp.model.Quote;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.mutiny.Multi;

@Path("/quotes")
public class QuotesResource {

    @Channel("quote-requests") Emitter<String> quoteRequestEmitter; // <1>

    @Channel("quotes") Multi<Quote> quotes;

    /**
 * Endpoint retrieving the "quotes" queue and sending the items to a server sent event.
 */
@GET
@Produces(MediaType.SERVER_SENT_EVENTS) // <2>
public Multi<Quote> stream() {
    return quotes; // <3>
}

    /**
     * Endpoint to generate a new quote request id and send it to "quote-requests" AMQP queue using the emitter.
     */
    @POST
    @Path("/request")
    @Produces(MediaType.TEXT_PLAIN)
    public String createRequest() {
        UUID uuid = UUID.randomUUID();
        quoteRequestEmitter.send(uuid.toString()); // <2>
        return uuid.toString();
    }
}
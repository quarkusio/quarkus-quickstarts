package org.acme.sqs;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.acme.sqs.model.Quark;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Path("/async/shield")
public class QuarksShieldAsyncResource {

    private static final Logger LOGGER = Logger.getLogger(QuarksShieldAsyncResource.class);

    @Inject
    SqsAsyncClient sqs;

    @ConfigProperty(name = "queue.url")
    String queueUrl;

    static ObjectReader QUARK_READER = new ObjectMapper().readerFor(Quark.class);

    @GET
    public Uni<List<Quark>> receive() {
        return Uni.createFrom()
            .completionStage(sqs.receiveMessage(m -> m.maxNumberOfMessages(10).queueUrl(queueUrl)))
            .onItem().transform(ReceiveMessageResponse::messages)
            .onItem().transform(m -> m.stream().map(Message::body).map(this::toQuark).collect(Collectors.toList()));
    }

    private Quark toQuark(String message) {
        Quark quark = null;
        try {
            quark = QUARK_READER.readValue(message);
        } catch (Exception e) {
            LOGGER.error("Error decoding message", e);
            throw new RuntimeException(e);
        }
        return quark;
    }
}
package org.acme.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.jbosslog.JBossLog;
import org.acme.sqs.model.Quark;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@JBossLog
@Path("/async/shield")
public class QuarksShieldAsyncResource {

    @Inject
    SqsAsyncClient sqs;

    @ConfigProperty(name = "queue.url")
    String queueUrl;

    static ObjectReader QUARK_READER = new ObjectMapper().readerFor(Quark.class);

    @GET
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Uni<List<Quark>> receive() {
        return Uni.createFrom()
            .completionStage(sqs.receiveMessage(m -> m.maxNumberOfMessages(10).queueUrl(queueUrl)))
            .onItem().transform(ReceiveMessageResponse::messages)
            .onItem().transform(m -> m.stream()
                        .map(Message::body)
                        .map(this::toQuark)
                        .collect(Collectors.toList()));
    }

    private Quark toQuark(String message) {
        Quark quark = null;
        try {
            quark = QUARK_READER.readValue(message);
        } catch (Exception e) {
            log.error("Error decoding message", e);
            throw new RuntimeException(e);
        }
        return quark;
    }
}

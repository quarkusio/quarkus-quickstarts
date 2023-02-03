package org.acme.sqs;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.acme.sqs.model.Quark;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Path("/async/cannon")
public class QuarksCannonAsyncResource {

    private static final Logger LOGGER = Logger.getLogger(QuarksCannonAsyncResource.class);

    @Inject
    SqsAsyncClient sqs;

    @ConfigProperty(name = "queue.url")
    String queueUrl;

    static ObjectWriter QUARK_WRITER = new ObjectMapper().writerFor(Quark.class);

    @POST
    @Path("/shoot")
    public Uni<Response> sendMessage(Quark quark) throws Exception {
        String message = QUARK_WRITER.writeValueAsString(quark);
        return Uni.createFrom()
            .completionStage(sqs.sendMessage(m -> m.queueUrl(queueUrl).messageBody(message)))
            .onItem().invoke(item -> LOGGER.infov("Fired Quark[{0}, {1}}]", quark.getFlavor(), quark.getSpin()))
            .onItem().transform(SendMessageResponse::messageId)
            .onItem().transform(id -> Response.ok().entity(id).build());
    }
}
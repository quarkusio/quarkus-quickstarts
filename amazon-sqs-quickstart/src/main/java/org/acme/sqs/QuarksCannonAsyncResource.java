package org.acme.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import lombok.extern.jbosslog.JBossLog;
import org.acme.sqs.model.Quark;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@JBossLog
@Path("/async/cannon")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class QuarksCannonAsyncResource {

    @Inject
    SqsAsyncClient sqs;

    @ConfigProperty(name = "queue.url")
    String queueUrl;

    static ObjectWriter QUARK_WRITER = new ObjectMapper().writerFor(Quark.class);

    @POST
    @Path("shoot")
    @Consumes(APPLICATION_JSON)
    public Uni<Response> sendMessage(Quark quark) throws Exception {

        String message = QUARK_WRITER.writeValueAsString(quark);

        return Uni.createFrom()
            .completionStage(sqs.sendMessage(m -> m.queueUrl(queueUrl).messageBody(message)))
            .onItem().invoke(item -> log.infov("Fired Quark[{0}, {1}}]", quark.getFlavor(), quark.getSpin()))
            .onItem().transform(SendMessageResponse::messageId)
            .onItem().transform(id -> Response.ok().entity(id).build());
    }
}

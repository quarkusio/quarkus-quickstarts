package org.acme.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import lombok.extern.jbosslog.JBossLog;
import org.acme.sqs.model.Quark;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@JBossLog
@Path("/sync/cannon")
@Produces(TEXT_PLAIN)
public class QuarksCannonSyncResource {

    @Inject
    SqsClient sqs;

    @ConfigProperty(name = "queue.url")
    String queueUrl;

    static ObjectWriter QUARK_WRITER = new ObjectMapper().writerFor(Quark.class);

    @POST
    @Path("shoot")
    @Consumes(APPLICATION_JSON)
    public Response sendMessage(Quark quark) throws Exception {
        String message = QUARK_WRITER.writeValueAsString(quark);
        SendMessageResponse response = sqs.sendMessage(m -> m.queueUrl(queueUrl).messageBody(message));
        log.infov("Fired Quark[{0}, {1}}]", quark.getFlavor(), quark.getSpin());
        return Response.ok().entity(response.messageId()).build();
    }
}

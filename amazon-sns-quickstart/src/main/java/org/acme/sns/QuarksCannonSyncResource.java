package org.acme.sns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.acme.sns.model.Quark;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Path("/sync/cannon")
@Produces(MediaType.TEXT_PLAIN)
public class QuarksCannonSyncResource {

    private static final Logger LOGGER = Logger.getLogger(QuarksCannonSyncResource.class);

    @Inject
    SnsClient sns;

    @ConfigProperty(name = "topic.arn")
    String topicArn;

    static ObjectWriter QUARK_WRITER = new ObjectMapper().writerFor(Quark.class);

    @POST
    @Path("/shoot")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response publish(Quark quark) throws Exception {
        String message = QUARK_WRITER.writeValueAsString(quark);
        PublishResponse response = sns.publish(p -> p.topicArn(topicArn).message(message));
        LOGGER.infov("Fired Quark[{0}, {1}}]", quark.getFlavor(), quark.getSpin());
        return Response.ok().entity(response.messageId()).build();
    }
}
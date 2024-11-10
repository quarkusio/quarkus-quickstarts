package org.acme.ses;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.ses.model.Email;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@Path("/async")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class QuarkusSesAsyncResource {

    @Inject
    SesAsyncClient ses;

    @POST
    @Path("/email")
    public Uni<String> encrypt(Email data) {
        return Uni.createFrom()
            .completionStage(
                ses.sendEmail(req -> req
                    .source(data.getFrom())
                    .destination(d -> d.toAddresses(data.getTo()))
                    .message(msg -> msg
                        .subject(sub -> sub.data(data.getSubject()))
                        .body(b -> b.text(txt -> txt.data(data.getBody()))))))
            .onItem().transform(SendEmailResponse::messageId);
    }
}
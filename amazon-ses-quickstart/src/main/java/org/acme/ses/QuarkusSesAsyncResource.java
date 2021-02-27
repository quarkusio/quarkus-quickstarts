package org.acme.ses;

import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.extern.jbosslog.JBossLog;
import org.acme.ses.model.Email;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@JBossLog
@Path("/async")
@Produces(TEXT_PLAIN)
@Consumes(APPLICATION_JSON)
public class QuarkusSesAsyncResource {

    @Inject
    SesAsyncClient ses;

    @POST
    @Path("email")
    public Uni<String> encrypt(Email data) {

        log.infof("Encrypting Email: From: %s To: %s...asynchronously", data.getFrom(), data.getTo());
        return Uni.createFrom().completionStage(
                ses.sendEmail(EmailHelper.createRequest(data)))
                .onItem().transform(SendEmailResponse::messageId);

    }
}

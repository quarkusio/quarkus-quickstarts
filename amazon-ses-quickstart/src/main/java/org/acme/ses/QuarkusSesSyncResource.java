package org.acme.ses;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.jbosslog.JBossLog;
import org.acme.ses.model.Email;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@JBossLog
@Path("/sync")
@Produces(TEXT_PLAIN)
@Consumes(APPLICATION_JSON)
public class QuarkusSesSyncResource {

    @Inject
    SesClient ses;

    @POST
    @Path("email")
    public String encrypt(Email data) {

        log.infof("Encrypting Email: From: %s To: %s...synchronously", data.getFrom(), data.getTo());
        return ses.sendEmail(EmailHelper.createRequest(data))
                .messageId();
    }
}

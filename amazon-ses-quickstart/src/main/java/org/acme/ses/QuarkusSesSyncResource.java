package org.acme.ses;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.acme.ses.model.Email;
import software.amazon.awssdk.services.ses.SesClient;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/sync")
@Produces(TEXT_PLAIN)
@Consumes(APPLICATION_JSON)
public class QuarkusSesSyncResource {

    @Inject
    SesClient ses;

    @POST
    @Path("email")
    public String encrypt(Email data) {

        return ses.sendEmail(EmailHelper.createRequest(data))
                .messageId();
    }
}

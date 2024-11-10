package org.acme.ses;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.ses.model.Email;
import software.amazon.awssdk.services.ses.SesClient;

@Path("/sync")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class QuarkusSesSyncResource {

    @Inject
    SesClient ses;

    @POST
    @Path("/email")
    public String encrypt(Email data) {
        return ses.sendEmail(req -> req
            .source(data.getFrom())
            .destination(d -> d.toAddresses(data.getTo()))
            .message(msg -> msg
                .subject(sub -> sub.data(data.getSubject()))
                .body(b -> b.text(txt -> txt.data(data.getBody()))))).messageId();
    }
}
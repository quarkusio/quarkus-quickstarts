package org.acme.ses;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
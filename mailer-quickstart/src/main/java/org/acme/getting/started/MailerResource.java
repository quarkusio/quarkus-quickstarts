package org.acme.getting.started;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.mailer.MailTemplate;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.qute.api.CheckedTemplate;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.util.concurrent.CompletionStage;

@Path("/mail")
public class MailerResource {

    @Inject
    ReactiveMailer mailer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<Response> greeting(
            @Valid @Email @QueryParam("email") String email,
            @Valid @NotBlank @QueryParam("name") String name) {
        return Templates.hello(name)
                .to(email)
                .subject("Ahoy " + name + "!")
                .send()
                .thenApply(x -> Response.accepted().build());
    }

    @CheckedTemplate
    static class Templates {
        public static native MailTemplate.MailTemplateInstance hello(String name);
    }


}
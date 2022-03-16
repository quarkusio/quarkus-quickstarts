package org.acme.extra;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.mailer.MailTemplate;
import io.quarkus.qute.CheckedTemplate;
import io.smallrye.mutiny.Uni;

@Path("/type-safe")
public class TypeSafeMailResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> greeting(
            @Valid @Email @RestQuery String email,
            @Valid @NotBlank @RestQuery String name) {
        return Templates.hello(name)
                .to(email)
                .subject("Ahoy " + name + "!")
                .send()
                .map(x -> Response.accepted().build());
    }

    @CheckedTemplate
    static class Templates {
        public static native MailTemplate.MailTemplateInstance hello(String name);
    }


}
package org.acme.extra;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
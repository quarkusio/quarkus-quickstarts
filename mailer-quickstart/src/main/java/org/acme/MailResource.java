package org.acme;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/mail")
public class MailResource {

    @Inject
    Mailer mailer;

    @GET
    @Blocking
    public void sendEmail() {
        mailer.send(Mail.withText("your-destination-email@quarkus.io", "Ahoy from Quarkus", "A simple email sent from a Quarkus application."));
    }

    @Inject
    ReactiveMailer reactiveMailer;

    @GET
    @Path("/reactive")
    public Uni<Void> sendEmailUsingReactiveMailer() {
        return reactiveMailer.send(                         // <4>
                Mail.withText("clement.escoffier@redhat.com",
                        "Ahoy from Quarkus",
                        "A simple email sent from a Quarkus application using the reactive API."
                )
        );
    }

}
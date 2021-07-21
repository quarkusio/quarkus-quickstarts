package org.acme.extra;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/extra")
public class ExtraMailResource {

    @Inject
    Mailer mailer;

    @GET
    @Path("/attachment")
    @Blocking
    public void sendEmailWithAttachment() {
        mailer.send(Mail.withText("your-destination-email@quarkus.io", "An email from quarkus with attachment",
                "This is my body")
                .addAttachment("my-file-1.txt",
                        "content of my file".getBytes(), "text/plain")
        );
    }

    @CheckedTemplate
    static class Templates {
        public static native MailTemplate.MailTemplateInstance hello(String name); // <1>
    }

    @GET
    @Path("/template")
    public Uni<Void> sendTypeSafeTemplate() {
        // the template looks like: Hello {name}!
        return Templates.hello("John")
                .to("your-destination-email@quarkus.io")
                .subject("Hello from Qute template")
                .send();
    }

    @Inject
    @Location("foo")
    MailTemplate foo; // <1>

    @GET
    @Path("/template2")
    public Uni<Void> sendTemplate2() {
        return foo.to("your-destination-email@quarkus.io") // <2>
                .subject("Hello from Qute template")
                .data("name", "John") // <3>
                .send(); // <4>
    }
}

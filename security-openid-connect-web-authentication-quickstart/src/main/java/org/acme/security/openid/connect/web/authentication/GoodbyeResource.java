package org.acme.security.openid.connect.web.authentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

/**
 * Landing page configured as {@code quarkus.oidc.logout.post-logout-path}. The OIDC provider
 * redirects here once RP-Initiated Logout has completed, so this path must stay public: the
 * local session cookie has already been cleared by the time the browser reaches it.
 */
@Path("/goodbye")
public class GoodbyeResource {

    @Inject
    Template goodbye;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return goodbye.instance();
    }
}

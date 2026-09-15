package org.acme.security.openid.connect.web.authentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

/**
 * Replaces the static {@code index.html} welcome page. Because this resource requires
 * an authenticated user, by the time it is reached the {@link IdToken} is guaranteed to
 * carry the username of the user who just completed the OpenID Connect Authorization Code Flow.
 */
@Path("/index.html")
public class WelcomeResource {

    @Inject
    @IdToken
    JsonWebToken idToken;

    @Inject
    Template welcome;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        String username = idToken.getClaim(Claims.preferred_username);
        return welcome.data("username", username);
    }
}

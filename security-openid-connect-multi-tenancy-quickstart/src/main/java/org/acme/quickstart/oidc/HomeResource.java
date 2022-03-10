package org.acme.quickstart.oidc;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;

@Path("/{tenant}")
public class HomeResource {

    /**
     * Injection point for the ID Token issued by the OpenID Connect Provider
     */
    @Inject
    @IdToken
    JsonWebToken idToken;

    /**
     * Returns the tokens available to the application. This endpoint exists only for demonstration purposes, you should not
     * expose these tokens in a real application.
     *
     * @return the landing page HTML
     */
    @GET
    @Produces("text/html")
    public String getHome() {
        StringBuilder response = new StringBuilder().append("<html>")
                .append("<body>");

        response.append("<h2>Welcome, ").append(this.idToken.getClaim("email").toString()).append("</h2>\n");
        response.append("<h3>You are accessing the application within tenant <b>").append(idToken.getIssuer()).append(" boundaries</b></h3>");

        return response.append("</body>").append("</html>").toString();
    }
}

package org.acme.quickstart.oidc;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

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

    @Inject
    JsonWebToken accessToken;

    /**
     * Returns the ID Token info. This endpoint exists only for demonstration purposes, you should not
     * expose this token in a real application.
     *
     * @return ID Token info
     */
    @GET
    @Produces("text/html")
    public String getIdTokenInfo() {
        StringBuilder response = new StringBuilder().append("<html>")
                .append("<body>");

        response.append("<h2>Welcome, ").append(this.idToken.getClaim("email").toString()).append("</h2>\n");
        response.append("<h3>You are accessing the application within tenant <b>").append(idToken.getIssuer()).append(" boundaries</b></h3>");

        return response.append("</body>").append("</html>").toString();
    }

    /**
     * Returns the Access Token info. This endpoint exists only for demonstration purposes, you should not
     * expose this token in a real application.
     *
     * @return Access Token info
     */
    @GET
    @Produces("text/html")
    @Path("bearer")
    public String getAccessTokenInfo() {
        StringBuilder response = new StringBuilder().append("<html>")
                .append("<body>");

        response.append("<h2>Welcome, ").append(this.accessToken.getClaim("email").toString()).append("</h2>\n");
        response.append("<h3>You are accessing the application within tenant <b>").append(accessToken.getIssuer()).append(" boundaries</b></h3>");

        return response.append("</body>").append("</html>").toString();
    }
}

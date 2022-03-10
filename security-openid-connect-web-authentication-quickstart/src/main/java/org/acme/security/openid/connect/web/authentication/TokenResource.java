package org.acme.security.openid.connect.web.authentication;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.RefreshToken;

@Path("/tokens")
public class TokenResource {

    /**
     * Injection point for the ID Token issued by the OpenID Connect Provider
     */
    @Inject
    @IdToken
    JsonWebToken idToken;

    /**
     * Injection point for the Access Token issued by the OpenID Connect Provider
     */
    @Inject
    JsonWebToken accessToken;

    /**
     * Injection point for the Refresh Token issued by the OpenID Connect Provider
     */
    @Inject
    RefreshToken refreshToken;

    /**
     * Returns the tokens available to the application. This endpoint exists only for demonstration purposes, you should not
     * expose these tokens in a real application.
     *
     * @return a map containing the tokens available to the application
     */
    @GET
    @Produces("text/html")
    public String getTokens() {
        StringBuilder response = new StringBuilder().append("<html>")
                .append("<body>")
                .append("<ul>");

        Object userName = this.idToken.getClaim("preferred_username");

        if (userName != null) {
            response.append("<li>username: ").append(userName.toString()).append("</li>");
        }

        Object scopes = this.accessToken.getClaim("scope");

        if (scopes != null) {
            response.append("<li>scopes: ").append(scopes.toString()).append("</li>");
        }

        response.append("<li>refresh_token: ").append(refreshToken.getToken() != null).append("</li>");

        return response.append("</ul>").append("</body>").append("</html>").toString();
    }
}

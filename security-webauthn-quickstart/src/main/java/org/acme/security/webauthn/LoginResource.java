package org.acme.security.webauthn;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestForm;

import io.quarkus.security.webauthn.WebAuthnLoginResponse;
import io.quarkus.security.webauthn.WebAuthnRegisterResponse;
import io.quarkus.security.webauthn.WebAuthnSecurity;
import io.vertx.ext.auth.webauthn.Authenticator;
import io.vertx.ext.web.RoutingContext;

@Path("")
public class LoginResource {

    @Inject
    WebAuthnSecurity webAuthnSecurity;

    @Path("/login")
    @POST
    @Transactional
    public Response login(@RestForm String userName,
                          @BeanParam WebAuthnLoginResponse webAuthnResponse,
                          RoutingContext ctx) {
        // Input validation
        if(userName == null || userName.isEmpty() || !webAuthnResponse.isSet() || !webAuthnResponse.isValid()) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        User user = User.findByUserName(userName);
        if(user == null) {
            // Invalid user
            return Response.status(Status.BAD_REQUEST).build();
        }
        try {
            Authenticator authenticator = this.webAuthnSecurity.login(webAuthnResponse, ctx).await().indefinitely();
            // bump the auth counter
            user.webAuthnCredential.counter = authenticator.getCounter();
            // make a login cookie
            this.webAuthnSecurity.rememberUser(authenticator.getUserName(), ctx);
            return Response.ok().build();
        } catch (Exception exception) {
            // handle login failure - make a proper error response
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @Path("/register")
    @POST
    @Transactional
    public Response register(@RestForm String userName,
                                  @BeanParam WebAuthnRegisterResponse webAuthnResponse,
                                  RoutingContext ctx) {
        // Input validation
        if(userName == null || userName.isEmpty() || !webAuthnResponse.isSet() || !webAuthnResponse.isValid()) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        User user = User.findByUserName(userName);
        if(user != null) {
            // Duplicate user
            return Response.status(Status.BAD_REQUEST).build();
        }
        try {
            // store the user
            Authenticator authenticator = this.webAuthnSecurity.register(webAuthnResponse, ctx).await().indefinitely();
            User newUser = new User();
            newUser.userName = authenticator.getUserName();
            WebAuthnCredential credential = new WebAuthnCredential(authenticator, newUser);
            credential.persist();
            newUser.persist();
            // make a login cookie
            this.webAuthnSecurity.rememberUser(newUser.userName, ctx);
            return Response.ok().build();
        } catch (Exception ignored) {
            // handle login failure
            // make a proper error response
            return Response.status(Status.BAD_REQUEST).build();
        }
    }
}
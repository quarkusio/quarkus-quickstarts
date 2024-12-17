package org.acme.security.webauthn;

import org.jboss.resteasy.reactive.RestForm;

import io.quarkus.security.webauthn.WebAuthnCredentialRecord;
import io.quarkus.security.webauthn.WebAuthnLoginResponse;
import io.quarkus.security.webauthn.WebAuthnRegisterResponse;
import io.quarkus.security.webauthn.WebAuthnSecurity;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("")
public class LoginResource {

    @Inject
    WebAuthnSecurity webAuthnSecurity;

    @Path("/login")
    @POST
    @Transactional
    public Response login(@BeanParam WebAuthnLoginResponse webAuthnResponse,
                          RoutingContext ctx) {
        // Input validation
        if(!webAuthnResponse.isSet() || !webAuthnResponse.isValid()) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        try {
            WebAuthnCredentialRecord credentialRecord = this.webAuthnSecurity.login(webAuthnResponse, ctx).await().indefinitely();
            User user = User.findByUsername(credentialRecord.getUsername());
            if(user == null) {
                // Invalid user
                return Response.status(Status.BAD_REQUEST).build();
            }
            // bump the auth counter
            user.webAuthnCredential.counter = credentialRecord.getCounter();
            // make a login cookie
            this.webAuthnSecurity.rememberUser(credentialRecord.getUsername(), ctx);
            return Response.ok().build();
        } catch (Exception exception) {
            // handle login failure - make a proper error response
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @Path("/register")
    @POST
    @Transactional
    public Response register(@RestForm String username,
                                  @BeanParam WebAuthnRegisterResponse webAuthnResponse,
                                  RoutingContext ctx) {
        // Input validation
        if(username == null || username.isEmpty() || !webAuthnResponse.isSet() || !webAuthnResponse.isValid()) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        User user = User.findByUsername(username);
        if(user != null) {
            // Duplicate user
            return Response.status(Status.BAD_REQUEST).build();
        }
        try {
            // store the user
            WebAuthnCredentialRecord credentialRecord = this.webAuthnSecurity.register(username, webAuthnResponse, ctx).await().indefinitely();
            User newUser = new User();
            newUser.username = credentialRecord.getUsername();
            WebAuthnCredential credential = new WebAuthnCredential(credentialRecord, newUser);
            credential.persist();
            newUser.persist();
            // make a login cookie
            this.webAuthnSecurity.rememberUser(newUser.username, ctx);
            return Response.ok().build();
        } catch (Exception ignored) {
            // handle login failure
            // make a proper error response
            return Response.status(Status.BAD_REQUEST).build();
        }
    }
}
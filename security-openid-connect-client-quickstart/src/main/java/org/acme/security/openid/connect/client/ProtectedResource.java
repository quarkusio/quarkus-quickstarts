package org.acme.security.openid.connect.client;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/protected")
@Authenticated
public class ProtectedResource {

    @Inject
    JsonWebToken principal;

    @GET
    @RolesAllowed("user")
    @Produces("text/plain")
    @Path("userName")
    public Uni<String> userName() {
        return Uni.createFrom().item(principal.getName());
    }
    
    @GET
    @RolesAllowed("admin")
    @Produces("text/plain")
    @Path("adminName")
    public Uni<String> adminName() {
        return Uni.createFrom().item(principal.getName());
    }
}

package org.acme.elytron.security.jpa.reactive;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/users")
public class UserResource {

    @GET
    @RolesAllowed("user")
    @Path("/me")
    @Produces(MediaType.TEXT_PLAIN)
    public String me(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

    @WithTransaction
    @POST
    @RolesAllowed("admin")
    public Uni<Void> create(String user) {
        return User.add(user, user, user).map(u -> null);
    }

}

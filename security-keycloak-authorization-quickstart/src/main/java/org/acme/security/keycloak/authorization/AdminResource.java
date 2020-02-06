package org.acme.security.keycloak.authorization;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/admin")
public class AdminResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String manage() {
        return "granted";
    }
}

package org.acme.keycloak.authorization;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api/public")
public class PublicResource {

    @GET
    public void serve() {
        // no-op
    }
}

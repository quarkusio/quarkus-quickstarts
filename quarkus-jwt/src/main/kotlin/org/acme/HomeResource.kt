package org.acme

import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/hello")
class HomeResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "hello"

    @GET
    @Path("/auth")
    @RolesAllowed("Admin")
    fun greet(@Context ctx: SecurityContext) = "Salaam Admin ... ${ctx.userPrincipal}"
}
package org.acme.security.jwt;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 * Version 3 of the TokenSecuredResource
 */
@Path("/secured-v3")
@RequestScoped
public class TokenSecuredResourceV3 {

    @Inject
    JsonWebToken jwt;

    @GET()
    @Path("permit-all")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s", name, ctx.isSecure(),
                ctx.getAuthenticationScheme());
        return helloReply;
    }

    @GET()
    @Path("roles-allowed")
    @RolesAllowed({ "Echoer", "Subscriber" })
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt.getClaimNames() != null;
        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(),
                ctx.getAuthenticationScheme(), hasJWT);
        return helloReply;
    }

    @GET
    @Path("winners")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("Subscriber")
    public String winners() {
        int remaining = 6;
        ArrayList<Integer> numbers = new ArrayList<>();

        // If the JWT contains a birthdate claim, use the day of the month as a pick
        if (jwt.containsClaim(Claims.birthdate.name())) {
            String bdayString = jwt.getClaim(Claims.birthdate.name());
            LocalDate bday = LocalDate.parse(bdayString);
            numbers.add(bday.getDayOfMonth());
            remaining--;
        }
        // Fill remaining picks with random numbers
        while (remaining > 0) {
            int pick = (int) Math.rint(64 * Math.random() + 1);
            numbers.add(pick);
            remaining--;
        }
        return numbers.toString();
    }
}

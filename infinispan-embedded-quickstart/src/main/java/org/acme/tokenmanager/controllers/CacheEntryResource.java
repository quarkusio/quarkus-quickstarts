package org.acme.tokenmanager.controllers;


import org.acme.tokenmanager.controllers.dtos.Entry;
import org.acme.tokenmanager.controllers.dtos.TokenInfo;
import org.acme.tokenmanager.services.CacheService;
import org.acme.tokenmanager.services.TokenInfoGenerator;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class CacheEntryResource {

    Logger logger = Logger.getLogger(CacheEntryResource.class);

    @Inject
    private CacheService cacheService;
    @Inject
    private TokenInfoGenerator tokenInfoGenerator;

    @GET
    @Path("tokens/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntry(@PathParam(value = "token") String token) {

        logger.debug("getEntry called");
        Entry entry = cacheService.getEntry(token);

        return Response.ok(entry)
                .build();
    }

    @POST
    @Path("tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEntry(@Valid Entry entry) {

        logger.debug("addEntry called");

        String tokenKey = cacheService.putInCache(entry);
        TokenInfo tokenInfo = tokenInfoGenerator.generateTokenInfo(tokenKey);

        return Response.created(tokenInfo.getUri())
                .entity(tokenInfo)
                .build();
    }
}
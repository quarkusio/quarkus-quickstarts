package org.acme.infinispanclient;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.acme.infinispanclient.model.HPCharacter;
import org.acme.infinispanclient.service.CharacterSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/harry-potter")
public class CharactersResource {

   private static final Logger LOGGER = LoggerFactory.getLogger("HarryPotterResource");

   @Inject
   private CharacterSearch searchService;

   @GET
   @Path("/{id}")
   @Produces(MediaType.TEXT_PLAIN)
   public HPCharacter byId(@PathParam("id") Integer id) {
      LOGGER.info("Search by Id " + id);
      HPCharacter character = searchService.getById(id);
      if (character == null) {
         throw new WebApplicationException("Character with id of " + id + " does not exist.", 404);
      }
      return character;
   }

   @GET
   @Path("/query")
   @Produces(MediaType.TEXT_PLAIN)
   public List<String> searchCharacter(@QueryParam("term") String term) {
      LOGGER.info("Search term");
      if(term == null) {
         return Collections.emptyList();
      }
      return searchService.search(term);
   }
}

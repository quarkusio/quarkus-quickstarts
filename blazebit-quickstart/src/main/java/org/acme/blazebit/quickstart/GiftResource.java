package org.acme.blazebit.quickstart;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blazebit.persistence.integration.jaxrs.EntityViewId;
import com.blazebit.persistence.view.EntityViewManager;

@Path("/gifts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GiftResource {

	@Inject
	EntityManager entityManager;

	@Inject
	EntityViewManager entityViewManager;

	@Inject
	SantaClausService santaClausService;

	@POST
	@Transactional
	public Response createGift(GiftUpdateView view) {
		entityViewManager.save(entityManager, view);
		return Response.created(URI.create("/gifts/" + view.getId())).build();
	}

	@GET
	public List<GiftView> getGifts() {
		return santaClausService.findAllGifts();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public GiftView updateGift(@EntityViewId("id") GiftUpdateView view) {
		entityViewManager.save(entityManager, view);
		return entityViewManager.find(entityManager, GiftView.class, view.getId());
	}

	@GET
	@Path("{id}")
	public GiftView getGift(@PathParam("id") Long id) {
		return entityViewManager.find(entityManager, GiftView.class, id);
	}
}
package org.acme.security.openid.connect.client;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.reactive.ClientWebApplicationException;

@Provider
public class FrontendExceptionMapper implements ExceptionMapper<ClientWebApplicationException> {

	@Override
	public Response toResponse(ClientWebApplicationException t) {
		return Response.status(t.getResponse().getStatus()).build();
	}

}

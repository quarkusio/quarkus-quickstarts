package org.acme.rest.client;

import org.jboss.resteasy.reactive.client.spi.ResteasyReactiveClientRequestContext;
import org.jboss.resteasy.reactive.client.spi.ResteasyReactiveClientRequestFilter;

import io.quarkus.arc.Arc;
import io.vertx.core.Vertx;

import javax.annotation.Priority;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class MyRequestFilter implements ResteasyReactiveClientRequestFilter {

    @Inject
    MyBean myRequestScopedBean;

    @Override
    @ActivateRequestContext
    public void filter(ResteasyReactiveClientRequestContext requestContext) {
        System.out.println("MyRequestFilter " + Arc.container().requestContext());
        System.out.println("Is active? " + Arc.container().requestContext().isActive());
        System.out.println("in req filter DC is " + Vertx.currentContext());
        myRequestScopedBean.log();
    }
}

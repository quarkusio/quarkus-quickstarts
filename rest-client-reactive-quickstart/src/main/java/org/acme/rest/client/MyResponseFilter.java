package org.acme.rest.client;

import org.jboss.resteasy.reactive.client.spi.ResteasyReactiveClientRequestContext;
import org.jboss.resteasy.reactive.client.spi.ResteasyReactiveClientResponseFilter;

import io.quarkus.arc.Arc;
import io.vertx.core.Vertx;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.ext.Provider;

@Provider
public class MyResponseFilter implements ResteasyReactiveClientResponseFilter{
    @Inject
    MyBean myRequestScopedBean;

    @Override
    @ActivateRequestContext
    public void filter(ResteasyReactiveClientRequestContext requestContext, ClientResponseContext responseContext) {
        System.out.println("Response filter called on " + Thread.currentThread().getName());
        System.out.println("MyResponseFilter " + Arc.container().requestContext() + " / " + Arc.container().requestContext().isActive());
        System.out.println("in resp filter DC is " + Vertx.currentContext());
        myRequestScopedBean.log();
    }
}

package org.acme.hibernate.orm;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import lombok.extern.jbosslog.JBossLog;

import io.quarkus.arc.Unremovable;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;

@JBossLog
@RequestScoped
@Unremovable
public class CustomTenantResolver implements TenantResolver {

    @Inject
    RoutingContext context;

    @Override
    public String getDefaultTenantId() {
        return "base";
    }

    @Override
    public String resolveTenantId() {
        String path = context.request().path();
        final String tenantId;
        if (path.startsWith("/mycompany")) {
            tenantId = "mycompany";
        } else {
            tenantId = getDefaultTenantId();
        }
        log.debugv("TenantId = {0}", tenantId);
        return tenantId;
    }

}

package org.acme.hibernate.orm;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import io.quarkus.arc.Arc;
import io.quarkus.arc.Unremovable;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;

@ApplicationScoped
@Unremovable
public class CustomTenantResolver implements TenantResolver {

    private static final Logger LOG = Logger.getLogger(CustomTenantResolver.class);
            
    @Override
    public String getDefaultHibernateOrmTenantId() {
        return "base";
    }
    
    @Override
    public String resolveHibernateOrmTenantId(RoutingContext context) {
        if (!Arc.container().requestContext().isActive()) {
            return getDefaultHibernateOrmTenantId();
        }
        String path = context.request().path();
        String[] parts = path.split("/");
        String tenantId;
        if (parts.length < 3) {
            // resolve to default tenant config
            tenantId = getDefaultHibernateOrmTenantId();
        } else {
            tenantId = parts[1];
        }
        LOG.debugv("TenantId = {0}", tenantId);
        return tenantId;
    }
    
}

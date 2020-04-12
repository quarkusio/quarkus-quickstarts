package org.acme.hibernate.orm;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

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
        String path = context.request().path();
        final String tenantId;
        if (path.startsWith("/mycompany")) {
            tenantId = "mycompany";
        } else {
            tenantId = getDefaultHibernateOrmTenantId();
        }
        LOG.debugv("TenantId = {0}", tenantId);
        return tenantId;
    }
    
}

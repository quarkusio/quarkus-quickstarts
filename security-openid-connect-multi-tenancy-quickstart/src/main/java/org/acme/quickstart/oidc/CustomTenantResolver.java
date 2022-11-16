package org.acme.quickstart.oidc;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.oidc.OidcRequestContext;
import io.quarkus.oidc.OidcTenantConfig;
import io.quarkus.oidc.OidcTenantConfig.ApplicationType;
import io.quarkus.oidc.TenantConfigResolver;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

@ApplicationScoped
public class CustomTenantResolver implements TenantConfigResolver {

    @Override 
    public Uni<OidcTenantConfig> resolve(RoutingContext context, OidcRequestContext<OidcTenantConfig> requestContext) {
        String path = context.request().path();
        
        if (path.startsWith("/tenant-a")) {
        
	        String keycloakUrl = ConfigProvider.getConfig().getValue("keycloak.url", String.class);
	        
	        OidcTenantConfig config = new OidcTenantConfig();
	        config.setTenantId("tenant-a");
	        config.setAuthServerUrl(keycloakUrl + "/realms/tenant-a");
	        config.setClientId("multi-tenant-client");
	        config.getCredentials().setSecret("secret");
	        config.setApplicationType(ApplicationType.HYBRID);
	        return Uni.createFrom().item(config);
        } else {
        	// resolve to default tenant config
            return Uni.createFrom().nullItem();
        }
    }
}

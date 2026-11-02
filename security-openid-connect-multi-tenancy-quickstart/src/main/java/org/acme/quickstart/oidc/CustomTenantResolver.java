package org.acme.quickstart.oidc;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.oidc.OidcRequestContext;
import io.quarkus.oidc.OidcTenantConfig;
import io.quarkus.oidc.TenantConfigResolver;
import io.quarkus.oidc.runtime.OidcUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

import static io.quarkus.oidc.runtime.OidcTenantConfig.ApplicationType.HYBRID;

@ApplicationScoped
public class CustomTenantResolver implements TenantConfigResolver {

    @Override 
    public Uni<OidcTenantConfig> resolve(RoutingContext context, OidcRequestContext<OidcTenantConfig> requestContext) {
        String path = context.request().path();

        if (path.startsWith("/tenant-a")) {
        
	        String keycloakUrl = ConfigProvider.getConfig().getValue("keycloak.url", String.class);
	        
	        OidcTenantConfig config = OidcTenantConfig.authServerUrl(keycloakUrl + "/realms/tenant-a")
                    .tenantId("tenant-a")
                    .clientId("multi-tenant-client")
                    .credentials("secret")
                    .applicationType(HYBRID)
                    .build();
	        return Uni.createFrom().item(config);
        } else {
            context.put(OidcUtils.TENANT_ID_ATTRIBUTE, OidcUtils.DEFAULT_TENANT_ID);
            return Uni.createFrom().nullItem();
        }
    }
}

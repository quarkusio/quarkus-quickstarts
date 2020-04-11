package org.acme.hibernate.orm;

import java.sql.Connection;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.ConfigProvider;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.jboss.logging.Logger;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.AgroalDataSourceConfiguration;
import io.quarkus.agroal.DataSource;
import io.quarkus.arc.Arc;
import io.quarkus.arc.Unremovable;
import io.quarkus.hibernate.orm.runtime.customized.QuarkusConnectionProvider;
import io.quarkus.hibernate.orm.runtime.tenant.DataSourceTenantConnectionResolver;
import io.quarkus.hibernate.orm.runtime.tenant.TenantConnectionResolver;

/**
 * Creates a database connection based on the data sources in the configuration file.
 * The tenant identifier is used as the data source name.
 * 
 * @author Michael Schnell
 *
 */
@ApplicationScoped
@Unremovable
public class CustomTenantConnectionResolver implements TenantConnectionResolver {

    private static final Logger LOG = Logger.getLogger(DataSourceTenantConnectionResolver.class);

    @Override
    public ConnectionProvider resolve(String tenantId) {

        LOG.debugv("resolve({0})", tenantId);

        final MultiTenancyStrategy strategy = configuredStrategy();
        LOG.debugv("multitenancy strategy: {0}", strategy);
        AgroalDataSource dataSource = tenantDataSource(tenantId, strategy);
        if (dataSource == null) {
            throw new IllegalStateException("No instance of datasource found for tenant: " + tenantId);
        }
        if (strategy == MultiTenancyStrategy.SCHEMA) {
            return new TenantConnectionProvider(tenantId, dataSource);
        }
        return new QuarkusConnectionProvider(dataSource);
    }

    /**
     * Create a new data source from the given configuration.
     * 
     * @param config Configuration to use.
     * 
     * @return New data source instance.
     */
    private static AgroalDataSource createFrom(AgroalDataSourceConfiguration config) {
        try {
            return AgroalDataSource.from(config);
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to create a new data source based on the default config", ex);
        }
    }

    /**
     * Returns either the default data source or the tenant specific one.
     * 
     * @param tenantId Tenant identifier. Required value that cannot be {@literal null}.
     * @param strategy Current multitenancy strategy Required value that cannot be {@literal null}.
     * 
     * @return Data source.
     */
    private static AgroalDataSource tenantDataSource(String tenantId, MultiTenancyStrategy strategy) {
        if (strategy == MultiTenancyStrategy.SCHEMA) {
            AgroalDataSource dataSource = Arc.container().instance(AgroalDataSource.class).get();
            return createFrom(dataSource.getConfiguration());
        }
        return Arc.container().instance(AgroalDataSource.class, new DataSource.DataSourceLiteral(tenantId)).get();
    }

    /**
     * Returns the configured strategy.
     * 
     * @return Multitenancy strategy.
     */
    private static MultiTenancyStrategy configuredStrategy() {
        return ConfigProvider.getConfig().getOptionalValue("quarkus.hibernate-orm.multitenant", MultiTenancyStrategy.class)
                .orElse(MultiTenancyStrategy.NONE);
    }

    private static class TenantConnectionProvider extends QuarkusConnectionProvider {

        private static final long serialVersionUID = 1L;

        private final String tenantId;

        public TenantConnectionProvider(String tenantId, AgroalDataSource dataSource) {
            super(dataSource);
            this.tenantId = tenantId;
        }

        @Override
        public Connection getConnection() throws SQLException {
            Connection conn = super.getConnection();
            conn.setSchema(tenantId);
            LOG.debugv("Set tenant {0} for connection: {1}", tenantId, conn);
            return conn;
        }

    }

}

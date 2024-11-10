package org.acme.liquibase;

import io.quarkus.liquibase.LiquibaseFactory;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSetStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MigrationService {
    // You can Inject the object if you want to use it manually
    @Inject
    LiquibaseFactory liquibaseFactory;

    public List<ChangeSetStatus> checkMigration() throws Exception {
        // Use the liquibase instance manually
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
            // Get the list of liquibase change set statuses
            return liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        }
    }
}
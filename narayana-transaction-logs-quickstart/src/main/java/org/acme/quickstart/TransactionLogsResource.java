package org.acme.quickstart;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;

import javax.sql.DataSource;
import java.sql.SQLException;

import static io.quarkus.datasource.common.runtime.DataSourceUtil.DEFAULT_DATASOURCE_NAME;

@Path("/transaction-logs")
public class TransactionLogsResource {

    @Inject
    DataSource dataSource;

    @Named("other-ds")
    @Inject
    DataSource otherDataSource;

    @Named("object-store-ds")
    @Inject
    DataSource objectStoreDataSource;

    @Inject
    TransactionManager transactionManager;

    @Inject
    UserTransaction userTransaction;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    @Path("/annotation-way")
    public boolean annotationWay(@RestQuery boolean rollback) throws SystemException {
        var result = makeTransaction();
        if (rollback) {
            transactionManager.getTransaction().setRollbackOnly();
        }
        return result;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/static-transaction-manager-way")
    public boolean staticTransactionManagerWay(@RestQuery boolean rollback) throws SystemException, NotSupportedException,
            HeuristicRollbackException, HeuristicMixedException, RollbackException {
        com.arjuna.ats.jta.TransactionManager.transactionManager().begin();
        var result = makeTransaction();
        if (rollback) {
            com.arjuna.ats.jta.TransactionManager.transactionManager().rollback();
        } else {
            com.arjuna.ats.jta.TransactionManager.transactionManager().commit();
        }
        return result;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/injected-transaction-manager-way")
    public boolean injectedTransactionManagerWay(@RestQuery boolean rollback) throws SystemException, NotSupportedException,
            HeuristicRollbackException, HeuristicMixedException, RollbackException {
        transactionManager.begin();
        var result = makeTransaction();
        if (rollback) {
            transactionManager.rollback();
        } else {
            transactionManager.commit();
        }
        return result;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/injected-user-transaction-way")
    public boolean injectedUserTransactionWay(@RestQuery boolean rollback) throws SystemException, NotSupportedException,
            HeuristicRollbackException, HeuristicMixedException, RollbackException {
        userTransaction.begin();
        var result = makeTransaction();
        if (rollback) {
            userTransaction.rollback();
        } else {
            userTransaction.commit();
        }
        return result;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/static-user-transaction-way")
    public boolean staticUserTransactionWay(@RestQuery boolean rollback) throws SystemException, NotSupportedException,
            HeuristicRollbackException, HeuristicMixedException, RollbackException {
        com.arjuna.ats.jta.UserTransaction.userTransaction().begin();
        var result = makeTransaction();
        if (rollback) {
            com.arjuna.ats.jta.UserTransaction.userTransaction().rollback();
        } else {
            com.arjuna.ats.jta.UserTransaction.userTransaction().commit();
        }
        return result;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/historical-data")
    public String inspectTransactionsLogHistoricalData() {
        // see all data that was written into JDBC object store
        // we back up all JDBC object store inserts so that we can inspect them after we execute transaction
        // such a table with historical data only makes sense for our tests and must not be used in production
        return makeQuery(objectStoreDataSource, "SELECT uidstring FROM quarkus_jbosststxtable_historical_data");
    }

    @Transactional
    @DELETE
    public void deleteObjectStoreHistoricalData() {
        // delete content of database table where we back up each insert into JDBC object store
        // it is only necessary for our tests so that we can run every test against clean table
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM quarkus_jbosststxtable_historical_data");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Path("/transaction-recovery")
    @Transactional
    @GET
    public void recovery() {
        insertCrashIntoAuditLog(DEFAULT_DATASOURCE_NAME, dataSource);
        insertCrashIntoAuditLog("object-store-ds", otherDataSource);
    }

    private boolean makeTransaction() {
        // involve two XA resources in a transaction so that JDBC object store is written into
        return getFirstExample(dataSource).equals(getFirstExample(otherDataSource));
    }

    private static String getFirstExample(DataSource dataSource) {
        return makeQuery(dataSource, "SELECT * FROM example WHERE data='first'");
    }

    private static String makeQuery(DataSource dataSource, String query) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery(query);
                if (result.next()) {
                    return result.getString(1);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertCrashIntoAuditLog(String dataSourceName, DataSource dataSource) {
        makeUpdateQuery(
                dataSource,
                "INSERT INTO audit_log (message, datasource) VALUES ('crash', '" + dataSourceName + "')"
        );
    }

    private static int makeUpdateQuery(DataSource dataSource, String query) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.createStatement()) {
                return statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

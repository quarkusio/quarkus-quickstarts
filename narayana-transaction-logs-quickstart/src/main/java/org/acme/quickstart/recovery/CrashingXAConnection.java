package org.acme.quickstart.recovery;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;
import org.postgresql.core.BaseConnection;
import org.postgresql.xa.PGXAConnection;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.SQLException;

public class CrashingXAConnection extends PGXAConnection {

    private static final Logger LOG = Logger.getLogger(CrashingXAConnection.class);
    private final PGXAConnection delegate;
    private volatile InstanceHandle<RoutingContext> routingContextInstanceHandle;

    CrashingXAConnection(PGXAConnection delegate, BaseConnection connection) throws SQLException {
        super(connection);
        this.delegate = delegate;
    }

    @Override
    public XAResource getXAResource() {
        return this;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        delegate.addConnectionEventListener(listener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        delegate.removeConnectionEventListener(listener);
    }

    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        delegate.addStatementEventListener(listener);
    }

    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        delegate.removeStatementEventListener(listener);
    }

    private RoutingContext getRoutingContext() {
        if (Arc.container() == null || !Arc.container().requestContext().isActive()) {
            // during transaction recovery, request context is not active
            return null;
        }

        if (routingContextInstanceHandle == null) {
            routingContextInstanceHandle = Arc.container().instance(RoutingContext.class);
        }

        return routingContextInstanceHandle.get();
    }

    private boolean shouldCrash() {
        var ctx = getRoutingContext();
        if (ctx == null) {
            return false;
        }
        return ctx.request().path().endsWith("/transaction-recovery");
    }

    @Override
    public void commit(Xid xid, boolean onePhase) throws XAException {
        if (shouldCrash()) {
            LOG.info("Crashing the system");
            Runtime.getRuntime().halt(1);
        }
        delegate.commit(xid, onePhase);
    }

    @Override
    public void end(Xid xid, int flags) throws XAException {
        delegate.end(xid, flags);
    }

    @Override
    public void forget(Xid xid) throws XAException {
        delegate.forget(xid);
    }

    @Override
    public int getTransactionTimeout() {
        return delegate.getTransactionTimeout();
    }

    @Override
    public boolean isSameRM(XAResource xares) throws XAException {
        if (xares instanceof CrashingXAConnection) {
            return delegate.isSameRM(((CrashingXAConnection) xares).delegate);
        }
        return delegate.isSameRM(xares);
    }

    @Override
    public int prepare(Xid xid) throws XAException {
        return delegate.prepare(xid);
    }

    @Override
    public Xid[] recover(int flag) throws XAException {
        return delegate.recover(flag);
    }

    @Override
    public void rollback(Xid xid) throws XAException {
        delegate.rollback(xid);
    }

    @Override
    public boolean setTransactionTimeout(int seconds) {
        return delegate.setTransactionTimeout(seconds);
    }

    @Override
    public void start(Xid xid, int flags) throws XAException {
        delegate.start(xid, flags);
    }
}

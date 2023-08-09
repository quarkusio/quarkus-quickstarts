package org.acme.quickstart.recovery;

import org.postgresql.core.BaseConnection;
import org.postgresql.xa.PGXAConnection;
import org.postgresql.xa.PGXADataSource;
import org.postgresql.xa.PGXADataSourceFactory;

import javax.naming.Reference;
import javax.sql.XAConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class CrashingPGXADataSource extends PGXADataSource {

    @Override
    public XAConnection getXAConnection() throws SQLException {
        return this.getXAConnection(this.getUser(), this.getPassword());
    }

    @Override
    public XAConnection getXAConnection(String user, String password) throws SQLException {
        Connection con = super.getConnection(user, password);
        return new CrashingXAConnection((PGXAConnection) super.getXAConnection(user, password), (BaseConnection) con);
    }

    @Override
    protected Reference createReference() {
        return new Reference(CrashingPGXADataSource.class.getName(), PGXADataSourceFactory.class.getName(), (String)null);
    }
}

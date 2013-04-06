package my.lib.data.jdbc;

import my.lib.data.trn.TrnResource;
import my.lib.util.data.DataSourceUtil;

import java.sql.Connection;

public class JdbcTrnResource implements TrnResource {

    private Connection connection;

    public JdbcTrnResource(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> T getRawResource() {
        return (T) connection;
    }

    @Override
    public void commit() {
        DataSourceUtil.commit(connection);
    }

    @Override
    public void rollback() {
        DataSourceUtil.rollback(connection);
    }

    @Override
    public void close() {
        DataSourceUtil.close(connection);
    }
}

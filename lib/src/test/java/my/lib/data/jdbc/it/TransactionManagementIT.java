package my.lib.data.jdbc.it;

import my.lib.TestResourceUtil;
import my.lib.data.jdbc.JdbcConnectionFactory;
import my.lib.data.jdbc.TestDataSourceUtil;
import my.lib.data.trn.DefaultTrnExecutor;
import my.lib.data.trn.TrnAction;
import my.lib.data.trn.TrnResource;
import my.lib.data.trn.TrnResourceRegistry;
import my.lib.util.data.DataSourceUtil;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TransactionManagementIT {

    private static final String PACKAGE_PATH = TestResourceUtil.getPackagePath(TransactionManagementIT.class);

    @Before
    public void before() {
        // 古いH2databaseのファイルを削除する。
        File file = new File("test/" + PACKAGE_PATH + "test.h2.db");
        if ( file.exists() ) {
            file.delete();
        }
    }

    @After
    public void after() {
        // H2databaseのファイルを削除する。
        File file = new File("test/" + PACKAGE_PATH + "test.h2.db");
        if ( file.exists() ) {
            file.delete();
        }
    }

    @Test
    public void testTransactionManagement_success() throws SQLException {
        Map<String, Object> jdbcConnConfig = new HashMap<String, Object>();
        jdbcConnConfig.put(JdbcConnectionFactory.CONFIG_DATASOURCE_FQCN, JdbcDataSource.class.getName());
        jdbcConnConfig.put(JdbcConnectionFactory.CONFIG_DATASOURCE_PROPERTIES + "URL", "jdbc:h2:test/" + PACKAGE_PATH + "test");
        jdbcConnConfig.put(JdbcConnectionFactory.CONFIG_DATASOURCE_PROPERTIES +"User", "sa");
        jdbcConnConfig.put(JdbcConnectionFactory.CONFIG_DATASOURCE_PROPERTIES +"Password", "sa");

        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory();
        jdbcConnectionFactory.initialize(jdbcConnConfig);

        DefaultTrnExecutor defaultTrnExecutor = new DefaultTrnExecutor();
        Map<String, Object> trnExecutorConfigMap = new HashMap<String, Object>();
        trnExecutorConfigMap.put(DefaultTrnExecutor.CONFIG_RESOURCE_FACTORY, jdbcConnectionFactory);
        defaultTrnExecutor.initialize(trnExecutorConfigMap);

        defaultTrnExecutor.execute(new TrnAction() {
            @Override
            public void run() {
                TrnResource trnResource = TrnResourceRegistry.peekCurrentTrnResource();
                Connection connection = trnResource.getRawResource();
                PreparedStatement preparedStatement = DataSourceUtil.createPreparedStatement(connection,
                        "CREATE TABLE hogehoge(num INT)");
                TestDataSourceUtil.execute(preparedStatement);
            }
        });

        defaultTrnExecutor.execute(new TrnAction() {
            @Override
            public void run() {
                TrnResource trnResource = TrnResourceRegistry.peekCurrentTrnResource();
                Connection connection = trnResource.getRawResource();
                PreparedStatement preparedStatement = DataSourceUtil.createPreparedStatement(connection,
                        "INSERT INTO hogehoge(num) VALUES(1)");
                TestDataSourceUtil.execute(preparedStatement);
            }
        });

        TrnResource resource = jdbcConnectionFactory.createResource();
        Connection connection = resource.getRawResource();
        ResultSet resultSet = TestDataSourceUtil.select(connection, "SELECT num FROM hogehoge");
        resultSet.next();
        assertThat(resultSet.getInt(1), is(1));
    }

    @Test
    public void testTransactionManagement_success_transactionFailed() throws SQLException {
        Map<String, Object> jdbcConnConfig = new HashMap<String, Object>();
        jdbcConnConfig.put(JdbcConnectionFactory.CONFIG_DATASOURCE_FQCN, JdbcDataSource.class.getName());
        jdbcConnConfig.put(JdbcConnectionFactory.CONFIG_DATASOURCE_PROPERTIES + "URL", "jdbc:h2:test/" + PACKAGE_PATH + "test");
        jdbcConnConfig.put(JdbcConnectionFactory.CONFIG_DATASOURCE_PROPERTIES +"User", "sa");
        jdbcConnConfig.put(JdbcConnectionFactory.CONFIG_DATASOURCE_PROPERTIES +"Password", "sa");

        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory();
        jdbcConnectionFactory.initialize(jdbcConnConfig);

        DefaultTrnExecutor defaultTrnExecutor = new DefaultTrnExecutor();
        Map<String, Object> trnExecutorConfigMap = new HashMap<String, Object>();
        trnExecutorConfigMap.put(DefaultTrnExecutor.CONFIG_RESOURCE_FACTORY, jdbcConnectionFactory);
        defaultTrnExecutor.initialize(trnExecutorConfigMap);

        defaultTrnExecutor.execute(new TrnAction() {
            @Override
            public void run() {
                TrnResource trnResource = TrnResourceRegistry.peekCurrentTrnResource();
                Connection connection = trnResource.getRawResource();
                PreparedStatement preparedStatement = DataSourceUtil.createPreparedStatement(connection,
                        "CREATE TABLE hogehoge(num INT)");
                TestDataSourceUtil.execute(preparedStatement);
            }
        });

        defaultTrnExecutor.execute(new TrnAction() {
            @Override
            public void run() {
                TrnResource trnResource = TrnResourceRegistry.peekCurrentTrnResource();
                Connection connection = trnResource.getRawResource();
                {
                    PreparedStatement preparedStatement = DataSourceUtil.createPreparedStatement(connection,
                            "INSERT INTO hogehoge(num) VALUES(1),(2)");
                    TestDataSourceUtil.execute(preparedStatement);
                }
                {
                    // failure
                    PreparedStatement preparedStatement = DataSourceUtil.createPreparedStatement(connection,
                            "INSERT INTO piyopiyo(num) VALUES(1),(2)");
                    TestDataSourceUtil.execute(preparedStatement);
                }
            }
        });

        TrnResource resource = jdbcConnectionFactory.createResource();
        Connection connection = resource.getRawResource();
        ResultSet resultSet = TestDataSourceUtil.select(connection, "SELECT COUNT(num) FROM hogehoge");
        resultSet.next();
        assertThat(resultSet.getInt(1), is(0));
    }
}

package my.lib.data.jdbc;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DataSourceConnectionManagerTest {

    @Test
    public void testInitialize_success() throws Exception {
        Map<String,Object> configMap = new HashMap<String, Object>();
        configMap.put(
                DataSourceConnectionManager.CONFIG_DATASOURCE_FQCN,
                JdbcDataSource.class.getName());
        configMap.put(
                DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "URL",
                "jdbc:h2:mem:");
        configMap.put(
                DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "User",
                "sa");
        configMap.put(
                DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "Password",
                "sa");

        DataSourceConnectionManager connectionManager = new DataSourceConnectionManager();
        connectionManager.initialize(configMap);

        assertThat(connectionManager.toString(),
                is("{ datasource=ds1: url=jdbc:h2:mem: user=sa, initialized=true }"));
    }

    @Test
    public void testGetConnection_success() throws Exception {
        Map<String, Object> configMap = new HashMap<String, Object>();
        configMap.put(
                DataSourceConnectionManager.CONFIG_DATASOURCE_FQCN,
                JdbcDataSource.class.getName());
        configMap.put(
                DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "URL",
                "jdbc:h2:mem:");
        configMap.put(
                DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "User",
                "sa");
        configMap.put(
                DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "Password",
                "sa");

        DataSourceConnectionManager connectionManager = new DataSourceConnectionManager();
        connectionManager.initialize(configMap);

        Connection connection = connectionManager.getConnection();

        assertNotNull(connection);
    }
}

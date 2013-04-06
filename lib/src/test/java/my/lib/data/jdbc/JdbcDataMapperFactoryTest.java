package my.lib.data.jdbc;

import my.lib.ContainsMatcher;
import my.lib.data.DataMapper;
import my.lib.data.jdbc.config.JdbcDataMapperConfig;
import my.lib.data.jdbc.config.NamedParameterSqlExpressionParser;
import my.lib.data.jdbc.config.SqlConfig;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertThat;

public class JdbcDataMapperFactoryTest {

    @Test
    public void testInitialize_success() throws Exception {
        // global configuration
        Map<String,Object> globalConfigMap = new HashMap<String, Object>();
        //
        // connection manager
        globalConfigMap.put(ConnectionManager.CONFIG_FQCN,
                DataSourceConnectionManager.class.getName());
        globalConfigMap.put(DataSourceConnectionManager.CONFIG_DATASOURCE_FQCN,
                JdbcDataSource.class.getName());
        globalConfigMap.put(DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "URL",
                "jdbc:h2:mem:");
        globalConfigMap.put(DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "User",
                "sa");
        globalConfigMap.put(DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "Password",
                "sa");
        //
        // sql expression parser
        globalConfigMap.put(JdbcDataMapperFactory.CONFIG_EXPRESSIONPARSER_FQCN,
                NamedParameterSqlExpressionParser.class.getName());
        //
        // mapping streategies
        globalConfigMap.put("dataaccess.mapper.jdbc.strategies.entity.fqcn",
                EntityRowMapper.class.getName());
        globalConfigMap.put("dataaccess.mapper.jdbc.strategies.entity.config.setterresolver.fqcn",
                UnderScoreSetterResolver.class.getName());

        // sql config
        Map<String, String> attrMap = new HashMap<String, String>();
        attrMap.put("id", "select-config");
        attrMap.put("target", CategoryConfig.class.getName());
        attrMap.put("strategy", "entity");
        SqlConfig sqlConfig = new SqlConfig("select-config", attrMap,
                "            SELECT DataAccess, Logging, Writer\n" +
                        "             FROM CategoryConfig\n" +
                        "             WHERE ConfigName = {ConfigName}");
        Map<String, SqlConfig> sqlConfigMap = new HashMap<String, SqlConfig>();
        sqlConfigMap.put("select-config", sqlConfig);

        JdbcDataMapperConfig dataMapperConfig = new JdbcDataMapperConfig(globalConfigMap, sqlConfigMap);

        Map<String, Object> dataMapperConfigMap = new HashMap<String, Object>();
        dataMapperConfigMap.put(JdbcDataMapperFactory.CONFIG_DATAMAPPER_CONFIG, dataMapperConfig);
        JdbcDataMapperFactory jdbcDataMapperFactory = new JdbcDataMapperFactory();
        jdbcDataMapperFactory.initialize(dataMapperConfigMap);

        assertThat(jdbcDataMapperFactory.toString(),
                allOf(new ContainsMatcher("{ initialized=true, connection manager={ datasource=ds"),
                        new ContainsMatcher(": url=jdbc:h2:mem: user=sa, initialized=true }, sql expression map={select-config={ id=select-config, sql=            SELECT DataAccess, Logging, Writer\n" +
                                "             FROM CategoryConfig\n" +
                                "             WHERE ConfigName = ?, parameters=[ConfigName], strategy=entity, target=class my.lib.data.jdbc.JdbcDataMapperFactoryTest$CategoryConfig }}," +
                                " mapping streategies={entity={ initialized=true, setter resolver={ my.lib.data.jdbc.UnderScoreSetterResolver } }} }")));
    }

    @Test
    public void testCreateDataMapper_success() throws Exception {
        // global configuration
        Map<String,Object> globalConfigMap = new HashMap<String, Object>();
        //
        // connection manager
        globalConfigMap.put(ConnectionManager.CONFIG_FQCN,
                DataSourceConnectionManager.class.getName());
        globalConfigMap.put(DataSourceConnectionManager.CONFIG_DATASOURCE_FQCN,
                JdbcDataSource.class.getName());
        globalConfigMap.put(DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "URL",
                "jdbc:h2:mem:");
        globalConfigMap.put(DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "User",
                "sa");
        globalConfigMap.put(DataSourceConnectionManager.CONFIG_DATASOURCE_OTHER_CONFIGS + "Password",
                "sa");
        //
        // sql expression parser
        globalConfigMap.put(JdbcDataMapperFactory.CONFIG_EXPRESSIONPARSER_FQCN,
                NamedParameterSqlExpressionParser.class.getName());
        //
        // mapping streategies
        globalConfigMap.put("dataaccess.mapper.jdbc.strategies.entity.fqcn",
                EntityRowMapper.class.getName());
        globalConfigMap.put("dataaccess.mapper.jdbc.strategies.entity.config.setterresolver.fqcn",
                UnderScoreSetterResolver.class.getName());

        // sql config
        Map<String, String> attrMap = new HashMap<String, String>();
        attrMap.put("id", "select-config");
        attrMap.put("target", CategoryConfig.class.getName());
        attrMap.put("strategy", "entity");
        SqlConfig sqlConfig = new SqlConfig("select-config", attrMap,
                "            SELECT DataAccess, Logging, Writer\n" +
                        "             FROM CategoryConfig\n" +
                        "             WHERE ConfigName = {ConfigName}");
        Map<String, SqlConfig> sqlConfigMap = new HashMap<String, SqlConfig>();
        sqlConfigMap.put("select-config", sqlConfig);

        JdbcDataMapperConfig dataMapperConfig = new JdbcDataMapperConfig(globalConfigMap, sqlConfigMap);

        Map<String, Object> dataMapperConfigMap = new HashMap<String, Object>();
        dataMapperConfigMap.put(JdbcDataMapperFactory.CONFIG_DATAMAPPER_CONFIG, dataMapperConfig);
        JdbcDataMapperFactory jdbcDataMapperFactory = new JdbcDataMapperFactory();
        jdbcDataMapperFactory.initialize(dataMapperConfigMap);

        DataMapper dataMapper = jdbcDataMapperFactory.createDataMapper();

        assertThat(dataMapper.toString(),
                allOf(new ContainsMatcher(
                        "{ query map={select-config={ id=select-config, sql=            SELECT DataAccess, Logging, Writer\n" +
                                "             FROM CategoryConfig\n" +
                                "             WHERE ConfigName = ?, parameters=[ConfigName], strategy=entity, target=class my.lib.data.jdbc.JdbcDataMapperFactoryTest$CategoryConfig }}, connection manager={ datasource=ds"),
                        new ContainsMatcher(": url=jdbc:h2:mem: user=sa, initialized=true }, row mappers={entity={ initialized=true, setter resolver={ my.lib.data.jdbc.UnderScoreSetterResolver } }} }")));
    }

    public static class CategoryConfig {
    }
}

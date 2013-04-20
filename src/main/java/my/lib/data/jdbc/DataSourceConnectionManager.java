package my.lib.data.jdbc;

import my.lib.util.CollectionUtil;
import my.lib.util.Predicate;
import my.lib.data.DataAccessException;
import my.lib.util.ClassUtil;
import my.lib.util.data.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

public class DataSourceConnectionManager implements ConnectionManager {

    public static final String CONFIG_DATASOURCE_FQCN = CONFIG_PREFIX + ".config.datasource.fqcn";

    public static final String CONFIG_DATASOURCE_OTHER_CONFIGS = CONFIG_PREFIX + ".config.datasource.";

    private DataSource dataSource;

    private boolean initialized = false;

    @Override
    public void initialize(Map<String, Object> config) {
        String dataSourceFqcn = (String) config.get(CONFIG_DATASOURCE_FQCN);
        boolean dataSourceFqcnRequired = dataSourceFqcn == null;
        if ( dataSourceFqcnRequired ) {
            throw new DataAccessException("データソースのFQCNが指定されていません: キー=" + CONFIG_DATASOURCE_FQCN);
        }

        this.dataSource = ClassUtil.createInstance(dataSourceFqcn);
        Map<String, String> otherConfigs = CollectionUtil.filterByKey(config, new Predicate<String>() {
            @Override
            public boolean evaluate(String s) {
                return (!CONFIG_DATASOURCE_FQCN.equals(s)) &&
                        s.startsWith(CONFIG_DATASOURCE_OTHER_CONFIGS);
            }
        });

        for ( String key : otherConfigs.keySet() ) {
            String setterValue = otherConfigs.get(key);
            String setterName = key.substring(CONFIG_DATASOURCE_OTHER_CONFIGS.length());
            ClassUtil.setProperty(dataSource, setterName, setterValue);
        }

        initialized = true;
    }

    @Override
    public Connection getConnection() {
        if ( ! initialized ) {
            throw new DataAccessException("初期化されていません。");
        }

        Connection connection = DataSourceUtil.getConnection(dataSource);

        return connection;
    }

    @Override
    public void commit(Connection connection) {
        DataSourceUtil.commit(connection);
    }

    @Override
    public void rollback(Connection connection) {
        DataSourceUtil.rollback(connection);
    }

    @Override
    public void close(Connection connection) {
        DataSourceUtil.close(connection);
    }

    @Override
    public String toString() {
        return "{ datasource=" + dataSource + ", initialized=" + initialized + " }";
    }
}

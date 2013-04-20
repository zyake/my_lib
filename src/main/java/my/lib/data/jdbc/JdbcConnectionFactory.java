package my.lib.data.jdbc;

import my.lib.util.CollectionUtil;
import my.lib.util.Predicate;
import my.lib.data.trn.TrnDataAccessException;
import my.lib.data.trn.TrnResource;
import my.lib.data.trn.TrnResourceFactory;
import my.lib.util.ClassUtil;
import my.lib.util.data.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;
import java.util.logging.Logger;

public class JdbcConnectionFactory implements TrnResourceFactory {

    private static final Logger LOGGER = Logger.getLogger(JdbcConnectionFactory.class.getName());

    public static final String CONFIG_DATASOURCE_FQCN = "trn.jdbc.datasource.fqcn";

    public static final String CONFIG_DATASOURCE_PROPERTIES = "trn.jdbc.datasource.properties.";

    private DataSource dataSource;

    private boolean initialized = false;

    @Override
    public void initialize(Map<String, Object> config) {
        String dataSourceClassName = (String) config.get(CONFIG_DATASOURCE_FQCN);
        boolean dataSourceClassNameRequired = dataSourceClassName == null;
        if ( dataSourceClassNameRequired ) {
            throw new TrnDataAccessException("データソースのFQCNが指定されていません: キー=" + CONFIG_DATASOURCE_FQCN);
        }

        Map<String, String> dataSourceProperties = CollectionUtil.filterByKey(config, new Predicate<String>() {
            @Override
            public boolean evaluate(String s) {
                return s.startsWith(CONFIG_DATASOURCE_PROPERTIES);
            }
        });

       this.dataSource = ClassUtil.createInstance(dataSourceClassName);
        for ( String key : dataSourceProperties.keySet() ) {
            String setter = key.substring(CONFIG_DATASOURCE_PROPERTIES.length());
            ClassUtil.setProperty(dataSource, setter, dataSourceProperties.get(key));
        }

        initialized = true;
    }

    @Override
    public TrnResource createResource() {
        if ( ! initialized ) {
            throw new TrnDataAccessException("初期化されていません。");
        }

        Connection connection = DataSourceUtil.getConnection(dataSource);
        TrnResource trnResource = new JdbcTrnResource(connection);

        return trnResource;
    }

    @Override
    public void finish() {
    }
}

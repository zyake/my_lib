package my.lib.data.jdbc;

import my.lib.CollectionUtil;
import my.lib.Predicate;
import my.lib.data.DataAccessException;
import my.lib.data.DataMapper;
import my.lib.data.DataMapperFactory;
import my.lib.data.jdbc.config.*;
import my.lib.util.ClassUtil;
import my.lib.util.io.FileUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JdbcDataMapperFactory implements DataMapperFactory {

    public static final String CONFIG_DATAMAPPER_CONFIG = "config.datamapper.config";

    public static final String CONFIG_EXPRESSIONPARSER_FQCN = "dataaccess.mapper.jdbc.sqlexpressionparser.fqcn";

    private ConnectionManager connectionManager;

    private Map<String, SqlExpression> sqlExpressionMap;

    private SqlExpressionParser expressionParser;

    private Map<String, RowMapper> mappingStrategyMap;

    private boolean initialized = false;

    /**
     * ファイルパスから設定を読み込み、データマッパを作成する。
     * @param filePath
     * @return
     */
    public static DataMapper createDataMapperFromFile(String filePath) {
        JdbcDataMapperConfigParser configParser = new SaxJdbcDataMapperConfigParser();
        InputStream inputStream = FileUtil.loadInputStream(filePath);
        JdbcDataMapperConfig dataMapperConfig = configParser.parseConfig(inputStream);

        JdbcDataMapperFactory jdbcDataMapperFactory = new JdbcDataMapperFactory();
        Map<String, Object> configMap = new HashMap<String, Object>();
        configMap.put(CONFIG_DATAMAPPER_CONFIG, dataMapperConfig);
        jdbcDataMapperFactory.initialize(configMap);

        return jdbcDataMapperFactory.createDataMapper();
    }

    @Override
    public void initialize(Map<String, Object> config) {
        JdbcDataMapperConfig dataMapperConfig = (JdbcDataMapperConfig) config.get(CONFIG_DATAMAPPER_CONFIG);
        boolean dataMapperConfigRequired = dataMapperConfig == null;
        if ( dataMapperConfigRequired ) {
            throw new DataAccessException(
                    "JDBCデータマッパの設定が見つかりませんでした: キー=" + CONFIG_DATAMAPPER_CONFIG);
        }

       loadGlobalConfig(dataMapperConfig.getGlobal());
       loadSqls(dataMapperConfig.getSqls());

        initialized = true;
    }

    private void loadGlobalConfig(Map<String, Object> global) {
        String connectionManagerFqcn = (String) global.get(ConnectionManager.CONFIG_FQCN);
        Class<ConnectionManager> connectionManagerClass = ClassUtil.classForName(connectionManagerFqcn, ConnectionManager.class);
        this.connectionManager = ClassUtil.createInstance(connectionManagerClass);

        Map<String, Object> connectionManagerConfigMap = CollectionUtil.filterByKey(global, new Predicate<String>() {
            @Override
            public boolean evaluate(String s) {
                return s.startsWith(ConnectionManager.CONFIG_PREFIX);
            }
        });
        connectionManager.initialize(connectionManagerConfigMap);

        String expressionParserFqcn = (String) global.get(CONFIG_EXPRESSIONPARSER_FQCN);
        Class<SqlExpressionParser> expressionParserClass = ClassUtil.classForName(expressionParserFqcn, SqlExpressionParser.class);
        this.expressionParser = ClassUtil.createInstance(expressionParserClass);

        loadMappingStrategies(global);
    }

    private void loadMappingStrategies(Map<String, Object> global) {
        // Path1. aggregate configurations by name
        Map<String, Object> strategyConfigMap = CollectionUtil.filterByKey(global, new Predicate<String>() {
            @Override
            public boolean evaluate(String s) {
                return s.startsWith(RowMapper.CONFIG_PREFIX);
            }
        });
        Map<String, MappingStrategy> strategyMap = new HashMap<String, MappingStrategy>();
        for ( String key : strategyConfigMap.keySet() ) {
            String truncatedName = key.substring(RowMapper.CONFIG_PREFIX.length());
            String strategyName = truncatedName.substring(0, truncatedName.indexOf("."));
            if ( ! strategyMap.containsKey( strategyName) ) {
                MappingStrategy mappingStrategy = new MappingStrategy(strategyName);
                strategyMap.put(strategyName, mappingStrategy);
            }
            String configKey = truncatedName.substring(strategyName.length() + 1); // without '.'!
            String configValue = (String) strategyConfigMap.get(key);
            strategyMap.get(strategyName).configMap.put(configKey, configValue);
        }

        // Path2. create RowMapper
        this.mappingStrategyMap = new HashMap<String, RowMapper>();
        for ( String key : strategyMap.keySet() ) {
            MappingStrategy mappingStrategy = strategyMap.get(key);
            String rowMapperFqcn = (String) mappingStrategy.configMap.get("fqcn");
            boolean rowMapperFqcnRequired = rowMapperFqcn == null;
            if ( rowMapperFqcnRequired ) {
                throw new DataAccessException(
                        "行マッピング設定の必須項目「fqcn」が指定されていません: キー=" + RowMapper.CONFIG_PREFIX + key + ".fqcn");
            }

            RowMapper rowMapper = ClassUtil.createInstance(rowMapperFqcn);
            rowMapper.initialize(mappingStrategy.configMap);
            mappingStrategyMap.put(key, rowMapper);
        }
    }

    private void loadSqls(Map<String, SqlConfig> sqls) {
        this.sqlExpressionMap = new HashMap<String, SqlExpression>();
        for ( String sqlId : sqls.keySet() ) {
            SqlConfig sqlConfig = sqls.get(sqlId);
            SqlExpression sqlExpression = expressionParser.parseExpression(sqlConfig);
            sqlExpressionMap.put(sqlId, sqlExpression);
        }
    }

    @Override
    public DataMapper createDataMapper() {
        if ( ! initialized ) {
            throw new DataAccessException("初期化されていません。");
        }

        return doCreateDataMapper();
    }

    @Override
    public String toString() {
        return "{ initialized=" + initialized +
                ", connection manager=" + connectionManager +
                ", sql expression map=" + sqlExpressionMap +
                ", mapping streategies=" + mappingStrategyMap + " }";
    }

    protected DataMapper doCreateDataMapper() {
        return new JdbcDataMapper(sqlExpressionMap, mappingStrategyMap, connectionManager);
    }

    private class MappingStrategy {

        private String name;

        private RowMapper rowMapper;

        private Map<String, Object> configMap = new HashMap<String, Object>();

        public MappingStrategy(String strategyName) {
            this.name = strategyName;
        }
    }
}

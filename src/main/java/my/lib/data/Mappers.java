package my.lib.data;

import my.lib.data.jdbc.JdbcDataMapperFactory;
import my.lib.data.jdbc.config.JdbcDataMapperConfig;
import my.lib.data.jdbc.config.JdbcDataMapperConfigParser;
import my.lib.data.jdbc.config.SaxJdbcDataMapperConfigParser;
import my.lib.util.io.FileUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Mappers {

    private Mappers() {
    }

    public static DataMapper createForJdbc(String filePath) {
        JdbcDataMapperConfigParser configParser = new SaxJdbcDataMapperConfigParser();
        InputStream inputStream = FileUtil.loadInputStream(filePath);
        JdbcDataMapperConfig dataMapperConfig = configParser.parseConfig(inputStream);

        JdbcDataMapperFactory jdbcDataMapperFactory = new JdbcDataMapperFactory();
        Map<String, Object> configMap = new HashMap<String, Object>();
        configMap.put(JdbcDataMapperFactory.CONFIG_DATAMAPPER_CONFIG, dataMapperConfig);
        jdbcDataMapperFactory.initialize(configMap);

        return jdbcDataMapperFactory.createDataMapper();
    }
}

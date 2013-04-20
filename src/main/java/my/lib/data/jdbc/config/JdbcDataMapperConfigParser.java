package my.lib.data.jdbc.config;

import java.io.InputStream;

public interface JdbcDataMapperConfigParser {

    JdbcDataMapperConfig parseConfig(InputStream inputStream);
}

package my.lib.data.jdbc.config;

import java.util.Collections;
import java.util.Map;

public class SqlConfig {

    private String id;

    private Map<String, String> attributes;

    private String sqlQuery;

    public SqlConfig(String id, Map<String, String> attributes, String sqlQuery) {
        this.id = id;
        this.attributes = Collections.unmodifiableMap(attributes);
        this.sqlQuery = sqlQuery;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{ id=" + id + ", sql=" + sqlQuery + ", attrs=" + attributes + " }";
    }
}

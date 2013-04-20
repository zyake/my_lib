package my.lib.data.jdbc.config;

import java.util.Collections;
import java.util.Map;

public class JdbcDataMapperConfig {

    private Map<String, Object> global;

    private Map<String, SqlConfig> sqls;

    public JdbcDataMapperConfig(Map<String, Object> global, Map<String, SqlConfig> sqls) {
        this.global = Collections.unmodifiableMap(global);
        this.sqls = Collections.unmodifiableMap(sqls);
    }

    public Map<String, Object> getGlobal() {
        return global;
    }

    public Map<String, SqlConfig> getSqls() {
        return sqls;
    }

    @Override
    public String toString() {
        return "{ global=" + global +", sqls=" + sqls + " }";
    }
}

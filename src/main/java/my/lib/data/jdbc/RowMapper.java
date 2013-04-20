package my.lib.data.jdbc;


import java.sql.ResultSet;
import java.util.Map;

public interface RowMapper {

    String CONFIG_PREFIX = "dataaccess.mapper.jdbc.strategies.";

    void initialize(Map<String, Object> config);

    /**
     * ResultSetの1行分の情報を元に、オブジェクトを作成する。
     * @param resultSet
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T mapRow(ResultSet resultSet, Class<T> clazz);
}

package my.lib.data.jdbc;

import my.lib.data.DataAccessException;
import my.lib.util.ClassUtil;
import my.lib.util.data.DataSourceUtil;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;

public class EntityRowMapper implements RowMapper {

    public static final String CONFIG_SETTERRESOLVER_FQCN = "config.setterresolver.fqcn";

    private SetterResolver setterResolver;

    private boolean initialized = false;

    @Override
    public void initialize(Map<String, Object> config) {
        String setterResolverFqcn = (String) config.get(CONFIG_SETTERRESOLVER_FQCN);
        boolean setterResolverRequired = setterResolverFqcn == null;
        if ( setterResolverRequired ) {
            throw new DataAccessException("セッターリゾルバが設定されていません: キー=" + CONFIG_SETTERRESOLVER_FQCN);
        }
        this.setterResolver = ClassUtil.createInstance(setterResolverFqcn);

        initialized = true;
    }

    @Override
    public <T> T mapRow(ResultSet resultSet, Class<T> clazz) {
        if ( ! initialized ) {
            throw new DataAccessException("初期化されていません。");
        }

        T target = ClassUtil.createInstance(clazz);

        ResultSetMetaData metaData = DataSourceUtil.getMetaData(resultSet);
        final int columnCount = DataSourceUtil.getColumnCount(metaData);
        for ( int i = 0 ; i < columnCount ; i ++ ) {
            String columnName = DataSourceUtil.getColumnName(metaData, i + 1);
            Object columnValue = DataSourceUtil.getObject(resultSet, i + 1);
            Method setter = setterResolver.resolveSetter(columnName, clazz);
            boolean setterRequired = setter == null;
            if ( setterRequired ) {
                throw new DataAccessException(
                        "対応するセッタが見つかりません: カラム名=" + columnName + ", オブジェクト=" + clazz);
            }

            ClassUtil.invokeMethod(setter, target, columnValue);
        }

        return target;
    }

    @Override
    public String toString() {
        return "{ initialized=" + initialized + ", setter resolver=" + setterResolver + " }";
    }
}

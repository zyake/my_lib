package my.lib.data.jdbc;

import my.lib.util.ClassUtil;
import my.lib.util.data.DataSourceUtil;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * JDBCのプレースホルダ「?」の位置に対応するパラメータを名前で
 * 埋め込むことができるSqlExpressionの実装。
 * <h2>例</h2>
 * <dl>
 *  <dt>クエリ</dt>
 *  <dd>SELECT name, department, birthday FROM employees WHERE department = ?</dd>
 *  <dt>パラメータ</dt>
 *  <dd>1番目のプレースホルダ=department</dd>
 *  <dt>実行時の指定方法</dt>
 *  <dd>
 *      <pre>
 *      Map params = new HashMap();
 *      params.put("department", "企画部");
 *      sqlExpression.evaluate(connection, params);
 *      </pre>
 *  </dd>
 *  </dl>
 */
public class NamedParameterSqlExpression implements SqlExpression {

    private String id;

    private List<String> parameters;

    private String sqlExpression;

    private String strategy;

    private Class target;

    public NamedParameterSqlExpression(String id, List<String> parameters, String sqlExpression, String strategy, Class target) {
        this.id = id;
        this.strategy = strategy;
        this.target = target;
        this.parameters = Collections.unmodifiableList(parameters);
        this.sqlExpression = sqlExpression;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getExpression() {
        return sqlExpression;
    }

    @Override
    public String getStrategy() {
        return strategy;
    }

    @Override
    public Class getTarget() {
        return target;
    }

    @Override
    public void setPlaceHolder(PreparedStatement preparedStatement, Map<String, Object> params) {
        for ( int i = 0 ; i < parameters.size() ; i ++ ) {
            String paramKey = parameters.get(i);
            Object paramValue = params.get(paramKey);
            DataSourceUtil.setObject(preparedStatement, i + 1, paramValue);
        }
    }

    @Override
    public void setPlaceHolder(PreparedStatement preparedStatement, Object paramSource) {
        for ( int i = 0 ; i < parameters.size() ; i ++ ) {
            String parameterKey = parameters.get(i);
            Object paramValue = ClassUtil.getProperty(paramSource, parameterKey);
            DataSourceUtil.setObject(preparedStatement, i + 1, paramValue);
        }
    }

    @Override
    public String toString() {
        return "{ id=" + id + ", sql=" + sqlExpression + ", parameters=" + parameters +
                ", strategy=" + strategy + ", target=" + target + " }";
    }
}

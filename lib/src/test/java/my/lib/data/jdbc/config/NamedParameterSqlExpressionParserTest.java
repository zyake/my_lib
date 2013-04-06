package my.lib.data.jdbc.config;

import my.lib.data.jdbc.SqlExpression;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NamedParameterSqlExpressionParserTest {

    @Test
    public void testParseExpression_success_singleParam() throws Exception {
        Map<String,String> attrMap = new HashMap<String, String>();
        attrMap.put("target", Object.class.getName());
        attrMap.put("id", "select-employees");
        attrMap.put("strategy", "entity");

        SqlConfig sqlConfig = new SqlConfig("select-employees",
                attrMap,
                "  SELECT * FROM employees WHERE  name = {name} AND department LIKE 'CUSTOMER%' ");

        SqlExpression sqlExpression = new NamedParameterSqlExpressionParser().parseExpression(sqlConfig);

        assertThat(sqlExpression.toString(),
                is("{ id=select-employees, sql=  SELECT * FROM employees WHERE  name = ? AND " +
                   "department LIKE 'CUSTOMER%' , parameters=[name], strategy=entity, target=class java.lang.Object }"));
    }

    @Test
    public void testParseExpression_success_singleQuote() throws Exception {
        Map<String,String> attrMap = new HashMap<String, String>();
        attrMap.put("target", Object.class.getName());
        attrMap.put("id", "select-employees");
        attrMap.put("strategy", "entity");

        SqlConfig sqlConfig = new SqlConfig("select-employees",
                attrMap,
                "  SELECT * FROM employees WHERE  name = {id} AND department LIKE '{name}'");

        SqlExpression sqlExpression = new NamedParameterSqlExpressionParser().parseExpression(sqlConfig);

        assertThat(sqlExpression.toString(),
                is("{ id=select-employees, sql=  SELECT * FROM employees WHERE  name = ? AND " +
                   "department LIKE '{name}', parameters=[id], strategy=entity, target=class java.lang.Object }"));
    }

    @Test
    public void testParseExpression_success_doubleQuote() throws Exception {
        Map<String,String> attrMap = new HashMap<String, String>();
        attrMap.put("target", Object.class.getName());
        attrMap.put("id", "select-employees");
        attrMap.put("strategy", "entity");

        SqlConfig sqlConfig = new SqlConfig("select-employees",
                attrMap,
                "  SELECT * FROM employees WHERE  name = {id} AND department LIKE \"{name}\"");

        SqlExpression sqlExpression = new NamedParameterSqlExpressionParser().parseExpression(sqlConfig);

        assertThat(sqlExpression.toString(),
                is("{ id=select-employees, sql=  SELECT * FROM employees WHERE  name = ? AND " +
                   "department LIKE \"{name}\", parameters=[id], strategy=entity, target=class java.lang.Object }"));
    }

    @Test
    public void testParseExpression_success_twoParameters() throws Exception {
        Map<String,String> attrMap = new HashMap<String, String>();
        attrMap.put("target", Object.class.getName());
        attrMap.put("id", "select-employees");
        attrMap.put("strategy", "entity");

        SqlConfig sqlConfig = new SqlConfig("select-employees",
                attrMap,
                "  SELECT * FROM employees WHERE  name = {id} AND department LIKE {name}");

        SqlExpression sqlExpression = new NamedParameterSqlExpressionParser().parseExpression(sqlConfig);

        assertThat(sqlExpression.toString(),
                is("{ id=select-employees, sql=  SELECT * FROM employees WHERE  name = ? AND " +
                   "department LIKE ?, parameters=[id, name], strategy=entity, target=class java.lang.Object }"));
    }

    @Test
    public void testParseExpression_success_containsComment() throws Exception {
        Map<String,String> attrMap = new HashMap<String, String>();
        attrMap.put("target", Object.class.getName());
        attrMap.put("id", "select-employees");
        attrMap.put("strategy", "entity");

        SqlConfig sqlConfig = new SqlConfig("select-employees",
                attrMap,
                "  SELECT /* {salary} / {department} */ * FROM employees WHERE  name = {id} AND department LIKE {name}");

        SqlExpression sqlExpression = new NamedParameterSqlExpressionParser().parseExpression(sqlConfig);

        assertThat(sqlExpression.toString(),
                is("{ id=select-employees, sql=  SELECT /* {salary} / {department} */ * FROM employees WHERE  name = ? AND " +
                   "department LIKE ?, parameters=[id, name], strategy=entity, target=class java.lang.Object }"));
    }
}

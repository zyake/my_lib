package my.lib.data.jdbc.config;

import my.lib.data.jdbc.SqlExpression;

public interface SqlExpressionParser {

    SqlExpression parseExpression(SqlConfig config);
}

package my.lib.data.jdbc;

import java.sql.PreparedStatement;
import java.util.Map;

public interface SqlExpression {

    String getId();

    String getExpression();

    String getStrategy();

    Class getTarget();

    void setPlaceHolder(PreparedStatement preparedStatement, Map<String, Object> params);

    void setPlaceHolder(PreparedStatement preparedStatement, Object paramSource);
}

package my.lib.util.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowHandler {

    void handleRow(ResultSet resultSet) throws SQLException;
}

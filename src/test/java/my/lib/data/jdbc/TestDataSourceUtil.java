package my.lib.data.jdbc;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class TestDataSourceUtil {

    public static DataSource createOnMemoryDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");

        return dataSource;
    }

    public static void execute(PreparedStatement statement) {
        try {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeQuery(Connection connection, String sql) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sql);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet select(Connection connection, String sql) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource createDataSource(String filePath) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:" + filePath);
        dataSource.setUser("sa");
        dataSource.setPassword("sa");

        return dataSource;
    }
}

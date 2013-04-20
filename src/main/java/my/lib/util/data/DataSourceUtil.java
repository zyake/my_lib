package my.lib.util.data;

import my.lib.util.UtilException;

import javax.sql.DataSource;
import java.sql.*;

public class DataSourceUtil {

    private DataSourceUtil() {
    }

    /**
     * AutoCommitをfalseに設定したConnectionを取得する。
     * @param dataSource
     * @return
     */
    public static Connection getConnection(DataSource dataSource) throws DataUtilException {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            return connection;
        } catch (SQLException e) {
            throw new DataUtilException("コネクションの取得に失敗しました: データソース=" + dataSource, e);
        }
    }

    public static void commit(Connection connection) throws DataUtilException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DataUtilException("コミットに失敗しました。", e);
        }
    }

    public static void rollback(Connection connection) throws DataUtilException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DataUtilException("ロールバックに失敗しました。", e);
        }
    }

    public static void close(Connection connection) throws DataUtilException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DataUtilException("クローズに失敗しました。", e);
        }
    }

    public static PreparedStatement createPreparedStatement(Connection connection, String sql) throws DataUtilException {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new DataUtilException("プリペアドステートメントの作成に失敗しました: SQL=" + sql, e);
        }
    }

    public static void setObject(PreparedStatement preparedStatement, int index, Object object) throws DataUtilException {
        try {
            preparedStatement.setObject(index, object);
        } catch (SQLException e) {
            throw new DataUtilException("プリペアステートメントへのパラメータの設定が失敗しました: " +
                    "インデックス=" + index + ", オブジェクト=" + object + ", ステートメント=" + preparedStatement, e);
        }
    }

    public static void executeQuery(PreparedStatement statement, RowHandler rowHandler) throws DataUtilException {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new DataUtilException("SQLの実行に失敗しました: ステートメント=" + statement, e);
        }
        try {
            while ( resultSet.next() ) {
                rowHandler.handleRow(resultSet);
            }
        } catch (SQLException e) {
            throw new DataUtilException("リザルトセットの操作に失敗しました。", e);
        }
    }

    public static ResultSetMetaData getMetaData(ResultSet resultSet) throws DataUtilException {
        try {
            return resultSet.getMetaData();
        } catch (SQLException e) {
            throw new DataUtilException("リザルトセットからメタデータを取得できませんでした。", e);
        }
    }

    public static int getColumnCount(ResultSetMetaData metaData) {
        try {
            return metaData.getColumnCount();
        } catch (SQLException e) {
            throw new UtilException("リザルトセットからカラム数の取得に失敗した。", e);
        }
    }

    public static String getColumnName(ResultSetMetaData metaData, int i) {
        try {
            return metaData.getColumnName(i);
        } catch (SQLException e) {
            throw new UtilException("リザルトセットからカラム名の取得に失敗しました。", e);
        }
    }

    public static Object getObject(ResultSet resultSet, int i) {
        try {
            return resultSet.getObject(i);
        } catch (SQLException e) {
            throw new UtilException("リザルトセットからオブジェクトの取得に失敗しました。", e);
        }
    }

    public static void addBatch(PreparedStatement preparedStatement) {
        try {
            preparedStatement.addBatch();
        } catch (SQLException e) {
            throw new UtilException(
                    "バッチ実行用のステートメントの追加に失敗しました: ステートメント=" + preparedStatement, e);
        }
    }

    public static int[] executeBatch(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new UtilException("バッチ実行に失敗しました: ステートメント=" + preparedStatement, e);
        }
    }
}

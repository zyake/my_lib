package my.lib.data.jdbc;

import my.lib.data.DataAccessException;
import my.lib.data.DataMapper;
import my.lib.util.data.DataSourceUtil;
import my.lib.util.data.RowHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcDataMapper implements DataMapper {

    private static final Logger LOGGER = Logger.getLogger(JdbcDataMapper.class.getName());

    private Map<String, SqlExpression> queryMap;

    private Map<String, RowMapper> rowMappers;

    private ConnectionManager connectionManager;

    public JdbcDataMapper(Map<String, SqlExpression> queryMap,
                          Map<String, RowMapper> rowMappers,
                          ConnectionManager connectionManager) {
        this.rowMappers = Collections.unmodifiableMap(rowMappers);
        this.queryMap = Collections.unmodifiableMap(queryMap);
        this.connectionManager = connectionManager;
    }

    @Override
    public <T> List<T> queryObjects(String queryId, Map<String, Object> condition) {
        if ( LOGGER.isLoggable(Level.FINE) ) {
            LOGGER.fine("start querying objects...: query id=" + queryId + ", condition=" + condition);
        }

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            final SqlExpression sqlExpression = queryMap.get(queryId);
            boolean sqlExpressionRequired = sqlExpression == null;
            if ( sqlExpressionRequired ) {
                throw new DataAccessException(
                        "クエリIDにマッチするクエリが見つかりませんでした: クエリID=" + queryId);
            }

            final RowMapper rowMapper = rowMappers.get(sqlExpression.getStrategy());
            boolean rowMapperRequired = rowMapper == null;
            if ( rowMapperRequired ) {
                throw new DataAccessException(
                        "戦略に対応する行マッパが見つかりませんでした: 戦略=" + sqlExpression.getStrategy());
            }

            PreparedStatement preparedStatement = DataSourceUtil.createPreparedStatement(connection,
                    sqlExpression.getExpression());
            sqlExpression.setPlaceHolder(preparedStatement, condition);
            if ( LOGGER.isLoggable(Level.FINER) ) {
                LOGGER.finer(
                        "prepared statement created: sql expression=" + sqlExpression +
                        ", statement=" + preparedStatement);
            }

            final List targetObjects = new ArrayList();
            DataSourceUtil.executeQuery(preparedStatement, new RowHandler() {
                @Override
                public void handleRow(ResultSet resultSet) throws SQLException {
                    Object target = rowMapper.mapRow(resultSet, sqlExpression.getTarget());
                    targetObjects.add(target);

                    if ( LOGGER.isLoggable(Level.FINER) ) {
                        LOGGER.finer("new mapped object created: object=" + target);
                    }
                }
            });

            if ( LOGGER.isLoggable(Level.FINE) ) {
                LOGGER.fine("succeeded querying objects: query id=" + queryId +
                        ", condition=" + condition + ", mapped object count=" + targetObjects.size());
            }

            return targetObjects;
        } catch (Exception ex) {
            if ( LOGGER.isLoggable(Level.SEVERE) ) {
                LOGGER.log(Level.SEVERE,
                        "failed querying objects: query id=" + queryId +", condition=" + condition,
                        ex);
            }

            throw new DataAccessException("セレクトの実行に失敗しました。", ex);
        } finally {
            if ( connection != null ) {
                connectionManager.close(connection);
            }
        }
    }

    @Override
    public <T> void insertObjects(String queryId, List<T> objects) {
        if ( LOGGER.isLoggable(Level.FINE) ) {
            LOGGER.fine("start inserting objects...: query id=" + queryId);
        }

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            SqlExpression sqlExpression = queryMap.get(queryId);
            boolean sqlExpressionRequired = sqlExpression == null;
            if ( sqlExpressionRequired ) {
                throw new DataAccessException(
                        "クエリIDにマッチするクエリが見つかりませんでした: クエリID=" + queryId);
            }

            String strategy = sqlExpression.getStrategy();
            RowMapper rowMapper = rowMappers.get(strategy);
            boolean rowMapperRequired = rowMapper == null;
            if ( rowMapperRequired ) {
                throw new DataAccessException(
                        "戦略に対応する行マッパが見つかりませんでした: 戦略=" + sqlExpression.getStrategy());
            }

            Class target = sqlExpression.getTarget();
            PreparedStatement preparedStatement =
                    DataSourceUtil.createPreparedStatement(connection, sqlExpression.getExpression());
            for ( T object : objects ) {
                boolean isNotTarget = object.getClass() != target;
                if ( isNotTarget ) {
                    throw new DataAccessException(
                            "オブジェクトはマッピング対象ではありません: 対象=" + target +
                            ", 実際のオブジェクト=" + object);
                }

                sqlExpression.setPlaceHolder(preparedStatement, object);
                if ( LOGGER.isLoggable(Level.FINER) ) {
                    LOGGER.finer("prepared statement created: sql expression=" + sqlExpression +
                            ", statement=" + preparedStatement);
                }

                DataSourceUtil.addBatch(preparedStatement);
            }

            int[] batchResult = DataSourceUtil.executeBatch(preparedStatement);
            if ( LOGGER.isLoggable(Level.FINE) ) {
                LOGGER.fine("succeeded inserting objects: query id=" + queryId +
                        ", batch result=" + Arrays.asList(batchResult));
            }
            connectionManager.commit(connection);
        } catch (Exception ex) {
            if ( LOGGER.isLoggable(Level.SEVERE) ) {
                LOGGER.log(Level.SEVERE, "failed inserting objects: query id=" + queryId, ex);
            }

            if ( connection != null ) {
                connectionManager.rollback(connection);
            }

            // 例外ハンドリングは、より上位のレイヤに移譲する。
            throw new DataAccessException("インサートの実行に失敗しました。", ex);
        } finally {
            if ( connection != null ) {
                connectionManager.close(connection);
            }
        }
    }

    @Override
    public String toString() {
        return "{ query map=" + queryMap +
                ", connection manager=" + connectionManager +
                ", row mappers=" + rowMappers + " }";
    }
}

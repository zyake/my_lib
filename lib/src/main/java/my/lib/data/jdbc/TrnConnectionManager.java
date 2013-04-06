package my.lib.data.jdbc;

import my.lib.data.DataAccessException;
import my.lib.data.trn.TrnResource;
import my.lib.data.trn.TrnResourceRegistry;

import java.sql.Connection;
import java.util.Map;

/**
 * トランザクション管理機能上で動作するコネクションマネージャの実装。
 */
public class TrnConnectionManager implements ConnectionManager {

    @Override
    public void initialize(Map<String, Object> config) {
    }

    @Override
    public Connection getConnection() {
        TrnResource trnResource = TrnResourceRegistry.peekCurrentTrnResource();
        if ( ! ( trnResource instanceof JdbcTrnResource) ) {
            throw new DataAccessException(
                    "現在のトランザクションリソースがJdbcTrnResourceではありません: 現在のトランザクションリソース=" + trnResource);
        }

        return trnResource.getRawResource();
    }

    @Override
    public void commit(Connection connection) {
        // トランザクションマネージャに処理を移譲する。
    }

    @Override
    public void rollback(Connection connection) {
        // トランザクションマネージャに処理を移譲する。
    }

    @Override
    public void close(Connection connection) {
        // トランザクションマネージャに処理を移譲する。
    }
}

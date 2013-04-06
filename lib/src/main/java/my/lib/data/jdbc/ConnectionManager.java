package my.lib.data.jdbc;

import my.lib.data.DataAccessException;

import java.sql.Connection;
import java.util.Map;

/**
 * コネクションを管理するためのマネージャ。
 *
 * コネクションの管理方法としては、getConnectionの実行ごとに
 * 実際にConnectionを払い出すローカル管理、Connectionを
 * 複数の呼び出しで共有し、トランザクションに関する操作は
 * 全てトランザクションマネージャに移譲するなどの方法が
 * 想定される。そのため、呼び出し元ではConnectionの取得、
 * commit,rollback,closeなどのトランザクションに関する操作は
 * 直接実行せず、Connectionのトランザクションに関する操作は
 * 全てConnectionManagerに移譲すること。
 */
public interface ConnectionManager {

    String CONFIG_FQCN = "dataaccess.mapper.jdbc.connectionmanager.fqcn";

    String CONFIG_PREFIX = "dataaccess.mapper.jdbc.connectionmanager";

    void initialize(Map<String, Object> config);

    Connection getConnection() throws DataAccessException;

    void commit(Connection connection) throws DataAccessException;

    void rollback(Connection connection) throws DataAccessException;

    void close(Connection connection) throws DataAccessException;
}

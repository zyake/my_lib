package my.lib.data;

import java.util.List;
import java.util.Map;

public interface DataMapper {

    /**
     * クエリの実行結果をオブジェクトのリストにマッピングした結果を返す。
     * クエリとマッピング対象のオブジェクトは一対一で紐付いており、
     * アプリケーション実行時において動的に変更することは、
     * 通常は考えられない。そのため、引数にはマッピング対象のオブジェクトの
     * 情報を渡す意味は無く、取得したオブジェクトのキャストは呼び出し元の
     * 責任で行うこと。
     * @param queryId
     * @param condition
     * @param <T>
     * @return
     * @throws
     */
    <T> List<T> queryObjects(String queryId, Map<String, Object> condition) throws DataAccessException;

    /**
     * リスト内のオブジェクトをそれぞれデータベースのエンティティにマッピングしてから挿入する。
     * @param queryId
     * @param objects
     * @param <T>
     * @throws
     */
    <T> void insertObjects(String queryId, List<T> objects) throws DataAccessException;
}

package my.lib.data.trn;

public interface TrnResource {

    <T> T getRawResource();

    void commit();

    void rollback();

    void close();
}

package my.lib.data;

public class DataAccessException extends RuntimeException {

    public DataAccessException(String msg) {
        super(msg);
    }

    public DataAccessException(String msg, Exception e) {
        super(msg, e);
    }
}

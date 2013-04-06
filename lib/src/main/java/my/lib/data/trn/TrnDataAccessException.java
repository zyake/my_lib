package my.lib.data.trn;

public class TrnDataAccessException extends RuntimeException {

    public TrnDataAccessException(String msg) {
        super(msg);
    }

    public TrnDataAccessException(String msg, Exception e) {
        super(msg, e);
    }
}

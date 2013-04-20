package my.lib.util;

public class UtilException extends RuntimeException {

    public UtilException(String msg, Exception e) {
        super(msg, e);
    }

    public UtilException(String msg) {
        super(msg);
    }
}

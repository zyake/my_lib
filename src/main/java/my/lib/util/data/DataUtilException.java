package my.lib.util.data;

import my.lib.util.UtilException;

public class DataUtilException extends UtilException {

    public DataUtilException(String msg) {
        super(msg);
    }

    public DataUtilException(String msg, Exception e) {
        super(msg, e);
    }
}

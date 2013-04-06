package my.lib.util.io;

import my.lib.util.UtilException;

public class IOUtilException extends UtilException {

    public IOUtilException(String msg) {
        super(msg);
    }

    public IOUtilException(String msg, Exception e) {
        super(msg, e);
    }
}

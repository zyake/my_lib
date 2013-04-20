package my.lib.util.xml;

import my.lib.util.UtilException;

public class XmlUtilException extends UtilException {

    public XmlUtilException(String msg) {
        super(msg);
    }

    public XmlUtilException(String msg, Exception e) {
        super(msg, e);
    }
}

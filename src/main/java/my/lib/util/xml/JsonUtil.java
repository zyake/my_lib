package my.lib.util.xml;


import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtil {

    private static volatile JsonFactory jsonFactory;

    private static final Object LOCK_OBJECT = new Object();

    private JsonUtil() {
    }

    public static JsonParser createJsonParser(InputStream inputStream) throws XmlUtilException {
        if ( jsonFactory == null ) {
            synchronized ( LOCK_OBJECT ) {
                if ( jsonFactory == null ) {
                    jsonFactory = new JsonFactory();
                }
            }
        }

        try {
            return jsonFactory.createJsonParser(inputStream);
        } catch (IOException e) {
            throw new XmlUtilException("JSONパーサの生成に失敗しました。", e);
        }
    }
}

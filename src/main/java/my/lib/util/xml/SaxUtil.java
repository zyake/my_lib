package my.lib.util.xml;

import my.lib.util.UtilException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class SaxUtil {

    private static final Object LOCK_OBJECT = new Object();

    private static volatile SAXParserFactory saxParserFactory = null;

    private SaxUtil() {
    }

    public static SAXParser createSaxParser() throws XmlUtilException {
        // 無駄だよね・・・。
        if ( saxParserFactory == null ) {
           synchronized ( LOCK_OBJECT ) {
               if ( saxParserFactory == null ) {
                   saxParserFactory = SAXParserFactory.newInstance();
               }
           }
        }

        try {
            return saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new XmlUtilException("SAXパーサの生成に失敗しました。", e);
        } catch (SAXException e) {
            throw new XmlUtilException("SAXパーサの生成に失敗しました。", e);
        }
    }

    public static void parseWithSAX(InputStream inputStream, DefaultHandler handler) throws XmlUtilException {
        SAXParser saxParser = createSaxParser();
        try {
            saxParser.parse(inputStream, handler);
        } catch (SAXException e) {
            throw new XmlUtilException("パースに失敗しました。", e);
        } catch (IOException e) {
            throw new XmlUtilException("パースに失敗しました。", e);
        }
    }
}

package my.lib.util.io;


import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class PropertiesUtil {

    private PropertiesUtil() {
    }

    public static Properties loadFromString(String text)  throws IOUtilException {
        Properties props = new Properties();
        try {
            props.load(new StringReader(text));
        } catch (IOException e) {
            throw new IOUtilException("プロパティの読み込みに失敗しました: プロパティ=" + text, e);
        }

        return props;
    }
}

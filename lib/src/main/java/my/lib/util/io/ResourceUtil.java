package my.lib.util.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceUtil {

    private ResourceUtil() {
    }

    public static InputStream loadStreamFromClassPath(String classPath) throws IOUtilException {
        return ResourceUtil.class.getClassLoader().getResourceAsStream(classPath);
    }

    public static InputStream loadStreamFromFilePath(String configPath) throws IOUtilException {
        try {
            return new FileInputStream(configPath);
        } catch (FileNotFoundException e) {
            throw new IOUtilException("ファイルが見つかりません: ファイルパス=" + configPath, e);
        }
    }
}

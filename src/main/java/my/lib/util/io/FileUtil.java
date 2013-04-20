package my.lib.util.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileUtil {

    private FileUtil() {
    }

    public static InputStream loadInputStream(String filePath) throws IOUtilException {
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new IOUtilException("ファイルが見つかりませんでした: パス=" + filePath, e);
        }
    }
}

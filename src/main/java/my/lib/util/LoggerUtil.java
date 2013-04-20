package my.lib.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LoggerUtil {

    private LoggerUtil() {
    }

    public static void readConfig(String loggerConfig) {
        try {
            InputStream inputStream = new ByteArrayInputStream(loggerConfig.getBytes());
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            throw new UtilException("ログの設定の読み込みに失敗しました.", e);
        }
    }
}

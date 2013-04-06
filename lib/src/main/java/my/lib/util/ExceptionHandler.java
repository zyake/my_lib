package my.lib.util;

import java.util.Map;

public interface ExceptionHandler {

    void initialize(Map<String, Object> config);

    void handleException(Exception ex);
}

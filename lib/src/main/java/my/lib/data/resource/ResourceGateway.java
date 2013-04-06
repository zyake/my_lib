package my.lib.data.resource;

import java.io.InputStream;
import java.util.Map;

public interface ResourceGateway {

    void initialize(Map<String, Object> config);

    InputStream getResource(String url);
}

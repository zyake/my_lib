package my.lib.data.resource;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ResourceParser<T> {

    void initialize(Map<String, Object> config);

    List<T> parseResource(InputStream resource);
}
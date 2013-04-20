package my.lib.data.resource;

import my.lib.data.DataAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class UrlResourceGateway implements ResourceGateway {

    @Override
    public void initialize(Map<String, Object> confnig) {
    }

    @Override
    public InputStream getResource(String url) {
        try {
            return (InputStream) new URL(url).getContent();
        } catch (IOException e) {
            throw new DataAccessException("URLによるデータアクセスに失敗しました: URL=" + url, e);
        }
    }
}

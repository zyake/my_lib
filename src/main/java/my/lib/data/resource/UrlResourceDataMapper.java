package my.lib.data.resource;

import my.lib.data.DataMapper;
import my.lib.util.StringUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UrlResourceDataMapper implements DataMapper {

    private Map<String, String> queryMap;

    private ResourceGateway gateway;

    private ResourceParser parser;

    private String url;

    public UrlResourceDataMapper(Map<String, String> queryMap, ResourceGateway gateway, ResourceParser parser, String url) {
        this.queryMap = Collections.unmodifiableMap(queryMap);
        this.gateway = gateway;
        this.parser = parser;
        this.url = url;
    }

    @Override
    public <T> List<T> queryObjects(String queryId, Map<String, Object> condition) {
        String query = queryMap.get(queryId);
        String queryUrl = constructQueryUrl(query, url, condition);
        InputStream resource = gateway.getResource(queryUrl);
        List<T> objects = (List<T>) parser.parseResource(resource);

        return objects;
    }

    @Override
    public <T> void insertObjects(String queryId, List<T> objects) {
        // TODO: そのうち作るかも!
        throw new NotImplementedException();
    }

    protected String constructQueryUrl(String query, String url, Map<String, Object> condition) {
        String modQuery = query;
        for ( String condKey : condition.keySet() ) {
            String condValue = (String) condition.get(condKey);
            String encodedCondValue = StringUtil.encodeUrl(condValue);
            modQuery = modQuery.replace("${" + condKey + "}", encodedCondValue);
        }

        return url + modQuery;
    }

    @Override
    public String toString() {
        return "{ query map=" + queryMap + ", gateway=" + gateway + ", parser=" + parser + ", url=" + url + " }";
    }
}

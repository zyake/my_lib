package my.lib.data.resource;

import my.lib.CollectionUtil;
import my.lib.Predicate;
import my.lib.data.DataAccessException;
import my.lib.data.DataMapper;
import my.lib.data.DataMapperFactory;
import my.lib.util.ClassUtil;

import java.util.Map;


public abstract class AbstractUrlResourceDataMapperFactory implements DataMapperFactory {

    public static final String DATAACCESS_DATAMAPPER_URL = "dataaccess.mapper.url";

    public static final String DATAACCESS_MAPPER_GATEWAY_FQCN = "dataaccess.mapper.gateway.fqcn";

    public static final String DATAACCESS_MAPPER_GATEWAY = "dataaccess.mapper.gateway.";

    public static final String DATAACCESS_MAPPER_PARSER_FQCN = "dataaccess.mapper.parser.fqcn";

    public static final String DATAACCESS_MAPPER_PARSER = "dataaccess.mapper.parser.";

    protected Map<String, String> queryMap;

    private String url;

    private Class<ResourceGateway> gatewayClass;

    private Map<String, Object> gatewayConfig;

    private Class parserClass;

    private Map<String, Object> parserConfig;

    private boolean initialized = false;

    @Override
    public void initialize(Map<String, Object> config) {
        url = (String) config.get(DATAACCESS_DATAMAPPER_URL);
        boolean urlRequired = url == null;
        if ( urlRequired ) {
            throw new DataAccessException(
                    "プロパティに接続先URLが指定されていません: プロパティキー=" + DATAACCESS_DATAMAPPER_URL);
        }
        loadQueryMap(config);
        loadGateway(config);
        loadParser(config);

        initialized = true;
    }

    @Override
    public DataMapper createDataMapper() {
        if ( ! initialized ) {
            throw new DataAccessException("ファクトリが初期化されていません。");
        }

        ResourceGateway gateway = ClassUtil.createInstance(gatewayClass);
        gateway.initialize(gatewayConfig);

        ResourceParser parser = ClassUtil.createInstance(parserClass);
        parser.initialize(parserConfig);

        UrlResourceDataMapper dataMapper = new UrlResourceDataMapper(queryMap, gateway, parser, url);

        return dataMapper;
    }

    private void loadParser(Map<String, Object> config) {
        String parserFqcn = (String) config.get(DATAACCESS_MAPPER_PARSER_FQCN);
        parserClass = ClassUtil.classForName(parserFqcn, ResourceParser.class);
        boolean parserFqcnRequired = parserFqcn == null;
        if ( parserFqcnRequired ) {
            throw new DataAccessException(
                    "プロパティにパーサのFQCNが指定されていません: プロパティキー=" + DATAACCESS_MAPPER_PARSER_FQCN);
        }
        parserConfig = CollectionUtil.filterByKey(config, new Predicate<String>() {
            @Override
            public boolean evaluate(String s) {
                return s.startsWith(DATAACCESS_MAPPER_PARSER);
            }
        });
    }

    private void loadGateway(Map<String, Object> config) {
        String gatewayFqcn = (String) config.get(DATAACCESS_MAPPER_GATEWAY_FQCN);
        boolean gatewayFqcnRequired = gatewayFqcn == null;
        if ( gatewayFqcnRequired ) {
            throw new DataAccessException(
                    "プロパティにゲートウェイのFQCNが指定されていません: プロパティキー=" + DATAACCESS_MAPPER_GATEWAY_FQCN);
        }
        gatewayClass = ClassUtil.classForName(gatewayFqcn, ResourceGateway.class);
        gatewayConfig = CollectionUtil.filterByKey(config, new Predicate<String>() {
            @Override
            public boolean evaluate(String s) {
                return s.startsWith(DATAACCESS_MAPPER_GATEWAY);
            }
        });
    }

    @Override
    public String toString() {
        return "{ initialized=" + initialized + ", gateway class=" + gatewayClass + ", gateway config=" + gatewayConfig +
                ", parser class=" + parserClass + ", parser config=" + parserConfig + ", query map=" + queryMap +
                ", url=" + url + " }";
    }

    /**
     * 設定情報を元に、クエリマップを読み込む。
     * クエリマップの読み込み元としては、ファイル、RDBMSなどが考えられるため、
     * 各具象クラスで実際にリソースを読み込む処理を実装すること。
     * @param config
     */
    protected abstract void loadQueryMap(Map<String, Object> config);
}

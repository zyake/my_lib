package my.lib.data;

import java.util.Map;

public interface DataMapperFactory {

    String DATAACCESS_MAPPER_FACTORYFQCN = "dataaccess.mapper.factory.fqcn";

    void initialize(Map<String, Object> config);

    DataMapper createDataMapper();
}

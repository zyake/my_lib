package my.lib.data.trn;

import java.util.Map;

public interface TrnExecutor {

    String CONFIG_EXCEPTION_HANDLER = "trn.config.exceptionhandler";

    String CONFIG_RESOURCE_FACTORY = "trn.config.resourcefactory";

    void initialize(Map<String, Object> config);

    void execute(TrnAction trnAction);
}

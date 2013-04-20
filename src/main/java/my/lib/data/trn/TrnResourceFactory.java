package my.lib.data.trn;

import java.util.Map;

public interface TrnResourceFactory {

    void initialize(Map<String, Object> config);

    TrnResource createResource();

    void finish();
}

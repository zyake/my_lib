package my.lib;

import java.util.HashMap;
import java.util.Map;

public class CollectionUtil {

    private CollectionUtil() {
    }

    public static <T1, T2> Map filterByKey(Map<T1, T2> map, Predicate<T1> filter) {
        Map newMap = new HashMap();
        for ( T1 key : map.keySet() ) {
            boolean accept = filter.evaluate(key);
            if ( accept ) {
                newMap.put(key,  map.get(key));
            }
        }

        return newMap;
    }
}

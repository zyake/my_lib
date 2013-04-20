package my.lib.util;

public class ArraysUtil {

    private ArraysUtil() {
    }

    public static <T> T selectOne(T[] array, Predicate<T> filter) {
        for ( T target : array ) {
            boolean accepted = filter.evaluate(target);
            if ( accepted ) {
                return target;
            }
        }

        return null;
    }
}

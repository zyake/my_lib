package my.lib.data.jdbc;

import java.lang.reflect.Method;

public interface SetterResolver {

    Method resolveSetter(String columnName, Class target);
}

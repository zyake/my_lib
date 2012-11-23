package my.lib.common;

import java.lang.reflect.Field;


public class ClassUtil {

	private ClassUtil() {
	}

	public static void setField(Object src, Field field, Object target) {
		field.setAccessible(true);
		try {
			field.set(target, src);
		} catch (IllegalArgumentException e) {
			throw new CommonException("illegal arguments", e);
		} catch (IllegalAccessException e) {
			throw new CommonException("illegal access", e);
		}
	}
}

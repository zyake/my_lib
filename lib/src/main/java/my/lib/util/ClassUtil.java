package my.lib.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassUtil {

    private ClassUtil() {
    }

    public static Class classForName(String fqcn, Class target) {
        Class targetClass = null;
        try {
            targetClass = Class.forName(fqcn);
        } catch (ClassNotFoundException e) {
            throw new UtilException("FQCNに該当するクラスが見つかりません: FQCN=" + fqcn, e);
        }

        boolean isAssignableFrom = target.isAssignableFrom(targetClass);
        if ( ! isAssignableFrom ) {
            throw new UtilException(
                    "指定されたFQCNのクラスは、意図した型と互換性がありません: FQCN=" +
                            fqcn + ", 互換性がある必要のある型=" + target);
        }

        return targetClass;
    }

    private static Class classForName(String fqcn) {
        Class targetClass = null;
        try {
            targetClass = Class.forName(fqcn);
        } catch (ClassNotFoundException e) {
            throw new UtilException("FQCNに該当するクラスが見つかりません: FQCN=" + fqcn, e);
        }

        return targetClass;
    }

    public static <T> T createInstance(Class clazz) {
        T newInstance = null;
        try {
            newInstance = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new UtilException("インスタンス生成に失敗しました: class=" + clazz, e);
        } catch (IllegalAccessException e) {
            throw new UtilException("インスタンス生成に失敗しました: class=" + clazz, e);
        }

        return newInstance;
    }

    public static <T> T createInstance(String fqcn) {
        Class aClass = classForName(fqcn);
        return createInstance(aClass);
    }

    public static Object invokeMethod(Method method, Object target, Object... params) {
        try {
            method.setAccessible(true);
            return method.invoke(target, params);
        } catch (IllegalAccessException e) {
            throw new UtilException("アクセスが不正です。", e);
        } catch (InvocationTargetException e) {
            throw new UtilException("メソッドの実行に失敗しました。", e);
        }
    }

    public static Object getProperty(Object source, String key) {
        try {
            String getterName = Character.toUpperCase(key.charAt(0)) + key.substring(1);
            Method getter = source.getClass().getMethod("get" + getterName, null);
            getter.setAccessible(true);
            return getter.invoke(source, null);
        } catch (NoSuchMethodException e) {
            throw new UtilException(
                    "ゲッターが見つかりませんでした: FQCN=" + source.getClass().getName() +
                    ", ゲッター=get" + key);
        } catch (InvocationTargetException e) {
            throw new UtilException(
                    "メソッドの実行に失敗しました: FQCN=" + source.getClass().getName() +
                    ", ゲッター=get" + key);
        } catch (IllegalAccessException e) {
            throw new UtilException(
                    "メソッドの実行に失敗しました: FQCN=" + source.getClass().getName() +
                    ", ゲッター=get" + key);
        }
    }

    public static void setProperty(Object source, String key, String value) {
        try {
            Method setter = source.getClass().getMethod("set" + key, String.class);
            setter.setAccessible(true);
            setter.invoke(source, value);
        } catch (NoSuchMethodException e) {
            throw new UtilException("セッターが見つかりませんでした: FQCN=" +
                    source.getClass().getName() + ", セッター=set" + key, e);
        } catch (InvocationTargetException e) {
            throw new UtilException("メソッドの実行に失敗しました: FQCN=" +
                    source.getClass().getName() + ", セッター=set" + key + ", 値=" + value, e);
        } catch (IllegalAccessException e) {
            throw new UtilException("メソッドの実行に失敗しました: FQCN=" +
                    source.getClass().getName() + ", セッター=set" + key + ", 値=" + value, e);
        }
    }

    public static void setField(Object entity, Field field, Object target) {
        field.setAccessible(true);
        try {
            field.set(target, entity);
        } catch (IllegalAccessException e) {
            throw new UtilException("フィールドへのアクセスに失敗しました。", e);
        }
    }
}

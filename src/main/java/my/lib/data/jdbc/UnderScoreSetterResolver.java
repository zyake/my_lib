package my.lib.data.jdbc;

import my.lib.util.ArraysUtil;
import my.lib.util.Predicate;

import java.lang.reflect.Method;

/**
 * アンダースコア「_」区切りのカラム名を元に、
 * 対応するセッターメソッドを取得するためのリゾルバの実装。
 */
public class UnderScoreSetterResolver implements SetterResolver {

    @Override
    public Method resolveSetter(String columnName, Class target) {
        String[] items = columnName.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("set");
        for ( String item : items ) {
            char firstChar = Character.toUpperCase(item.charAt(0));
            String restString = item.substring(1).toLowerCase();
            stringBuilder
                .append(firstChar)
                .append(restString);
        }

        final String setterName = stringBuilder.toString();

        Method targetSetter = ArraysUtil.selectOne(
                target.getMethods(),
                new Predicate<Method>() {
                    @Override
                    public boolean evaluate(Method method) {
                        return method.getName().equals(setterName);
                    }
                });

        return targetSetter;
    }

    @Override
    public String toString() {
        return "{ " + getClass().getName() + " }";
    }
}

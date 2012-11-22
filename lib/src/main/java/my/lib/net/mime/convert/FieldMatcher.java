package my.lib.net.mime.convert;

import java.lang.reflect.Field;

import my.lib.net.mime.BodyPart;

public interface FieldMatcher {

	Field findMatchedField(BodyPart bodyPart, Class clazz);
}

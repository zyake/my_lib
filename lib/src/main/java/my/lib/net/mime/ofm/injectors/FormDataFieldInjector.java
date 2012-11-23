package my.lib.net.mime.ofm.injectors;

import java.lang.reflect.Field;

import my.lib.common.ClassUtil;
import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEUtil;
import my.lib.net.mime.ofm.EntityInjector;
import my.lib.net.mime.ofm.MIMEConvertException;

/**
 * multipart/form-data形式のエンティティをオブジェクトのフィールドに挿入するインジェクタ。
 * @author zyake
 */
public class FormDataFieldInjector implements EntityInjector {

	public void inject(BodyPart body, Object entity, Object target) {
		Field field = findMatchedField(body, target.getClass());
		ClassUtil.setField(entity, field, target);
	}

	private Field findMatchedField(BodyPart bodyPart, Class clazz) {
		String dispositionName = MIMEUtil.getDispositionName(bodyPart);
		boolean missingDispositionName = dispositionName == null;
		if ( missingDispositionName ) {
			throw new MIMEConvertException(
					"missing Content-Disposition field: " + bodyPart);
		}

		for( Field field : clazz.getDeclaredFields() ) {
			boolean matchedFieldFound = dispositionName.equals(field.getName());
			if ( matchedFieldFound ) {
				return field;
			}
		}

		throw new MIMEConvertException("appropirate field not found: name=" + dispositionName);
	}
}

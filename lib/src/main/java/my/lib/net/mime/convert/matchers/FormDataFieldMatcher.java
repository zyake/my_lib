package my.lib.net.mime.convert.matchers;

import java.lang.reflect.Field;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEHeader;
import my.lib.net.mime.MIMEParam;
import my.lib.net.mime.convert.FieldMatcher;
import my.lib.net.mime.convert.MIMEConvertException;

/**
 * multipart/form-data形式データの各ボディパートと、
 * オブジェクトのフィールドを名前でマッチングするマッチャの実装。
 * 
 * @author zyake
 */
public class FormDataFieldMatcher implements FieldMatcher {

	public Field findMatchedField(BodyPart bodyPart, Class clazz) {
		String dispositionName = null;
		DISOSITION_SEARCH:
		for ( MIMEHeader header : bodyPart.getHeaders() ) {
			boolean targetFieldFound = "Content-Disposition".equals(header.getFieldName());
			if ( !targetFieldFound ) {
				continue;
			}
			
			for ( MIMEParam param : header.getParams() ) {
				boolean targetParamFound = "name".equals(param.getKey());
				if ( targetParamFound ) {
					dispositionName = param.getValue();
					break DISOSITION_SEARCH;
				}
			}
		}
		
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

		return null;
	}

}

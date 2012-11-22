package my.lib.net.mime.convert;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import my.lib.common.Assert;
import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MultipartMessage;

public class DefaultMultipartMessageMapper implements MultipartMessageMapper {

	private List<EntityConverter> converters;
	
	private List<FieldMatcher> matchers;
	
	public DefaultMultipartMessageMapper(
			List<EntityConverter> converters,
			List<FieldMatcher> matchers) {
		this.converters = converters;
		this.matchers = matchers;
	}
	
	public void mapToObject(MultipartMessage msg, Object target) {	
		Assert.notNull(msg, "msg");
		Assert.notNull(target, "target");
		
		for ( BodyPart bodyPart : msg.getBodyParts() ) {
			EntityConverter matchedConvereter = null;
			for ( EntityConverter convereter : converters ) {
				boolean accepted = convereter.accept(bodyPart);
				if ( !accepted ) {
					continue;
				}
				
				matchedConvereter = convereter;
				break;
			}
			
			boolean converterNotFound = matchedConvereter == null;
			if ( converterNotFound ) {
				throw new MIMEConvertException("matched converter not found: " + msg);
			}
			
			Object convertedEntity = matchedConvereter.convert(bodyPart);

			Field targetField = findField(bodyPart, target);
			
			try {
				targetField.set(target, convertedEntity);
			} catch (IllegalArgumentException e) {
				throw new MIMEConvertException(e);
			} catch (IllegalAccessException e) {
				throw new MIMEConvertException(e);
			}
		}
	}

	private Field findField(BodyPart bodyPart, Object target) {
		for ( FieldMatcher matcher : matchers ) {
			Field matchedField = matcher.findMatchedField(bodyPart, target.getClass());
			boolean matchedFieldFound = matchedField != null;
			if ( matchedFieldFound ) {
				return matchedField;
			}
		}
		
		throw new MIMEConvertException(
					"matched field not found: body part=" + bodyPart + 
					", target class=" + target.getClass());
	}

	public List<EntityConverter> getConverters() {
		return Collections.unmodifiableList(converters);
	}

	public List<FieldMatcher> getMatchers() {
		return Collections.unmodifiableList(matchers);
	}
}

package my.lib.net.mime.ofm;

import java.util.Collections;
import java.util.List;

import my.lib.common.Assert;
import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MultipartMessage;

public class DefaultMultipartMessageMapper implements MultipartMessageMapper {

	private List<EntityConverter> converters;

	private EntityInjector injector;

	protected DefaultMultipartMessageMapper(List<EntityConverter> converters, EntityInjector injector) {
		this.converters = converters;
		this.injector = injector;
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

			Object convertedEntity = matchedConvereter.convertEntity(bodyPart);
			injector.inject(bodyPart, convertedEntity, target);
		}
	}

	public List<EntityConverter> getConverters() {
		return Collections.unmodifiableList(converters);
	}

	public EntityInjector getInjector() {
		return injector;
	}
}

package my.lib.net.mime.ofm;

import my.lib.common.Assert;
import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MultipartMessage;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultMultipartMessageMapper implements MultipartMessageMapper {

    private static Logger LOG = Logger.getLogger(DefaultMultipartMessageMapper.class.getName());

	private List<EntityConverter> converters;

	private EntityInjector injector;

	public DefaultMultipartMessageMapper(List<EntityConverter> converters, EntityInjector injector) {
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
				throw new MIMEConvertException("matched converter not found: message=" + bodyPart);
			}

			Object convertedEntity = matchedConvereter.convertEntity(bodyPart);
			injector.inject(bodyPart, convertedEntity, target);
		}

        if ( LOG.isLoggable(Level.INFO) ) {
            LOG.info("object injection successed: " + target);
        }
	}

	public List<EntityConverter> getConverters() {
		return Collections.unmodifiableList(converters);
	}

	public EntityInjector getInjector() {
		return injector;
	}
}

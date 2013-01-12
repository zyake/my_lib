package my.lib.net.mime.ofm;

import my.lib.common.Assert;
import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MultipartMessage;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class DefaultMultipartMessageMapper implements MultipartMessageMapper {

    private static Logger LOG = Logger.getLogger(DefaultMultipartMessageMapper.class.getName());

	private List<ConverterHolder> holders;

	private EntityInjector injector;

	public DefaultMultipartMessageMapper(List<ConverterHolder> holders, EntityInjector injector) {
		this.holders = Collections.unmodifiableList(holders);
		this.injector = injector;
	}

	public void mapToObject(MultipartMessage msg, Object target) {
		Assert.notNull(msg, "msg");
		Assert.notNull(target, "target");

		for ( BodyPart bodyPart : msg.getBodyParts() ) {
			EntityConverter matchedConvereter = null;
			for ( ConverterHolder holder : holders ) {
                EntityConverter converter = holder.getConverter();
                EntityAcceptor acceptor = holder.getAcceptor();
                boolean accepted = acceptor.accept(bodyPart);
				if ( !accepted ) {
					continue;
				}

				matchedConvereter = converter;
				break;
			}

			boolean converterNotFound = matchedConvereter == null;
			if ( converterNotFound ) {
				throw new MIMEConvertException("matched converter not found: message=" + bodyPart);
			}

			Object convertedEntity = matchedConvereter.convertEntity(bodyPart);
			injector.inject(bodyPart, convertedEntity, target);
		}
	}

	public List<ConverterHolder> getHolders() {
        return holders;
	}

	public EntityInjector getInjector() {
		return injector;
	}
}

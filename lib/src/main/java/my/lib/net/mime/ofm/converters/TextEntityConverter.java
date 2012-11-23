package my.lib.net.mime.ofm.converters;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.AbstractEntityConverter;
import my.lib.net.mime.ofm.EntityAcceptor;

public class TextEntityConverter extends AbstractEntityConverter {

	public TextEntityConverter(EntityAcceptor acceptor) {
		super(acceptor);
	}

	@Override
	public Object convertEntity(BodyPart bodyPart) {
		return bodyPart.getEntity();
	}
}

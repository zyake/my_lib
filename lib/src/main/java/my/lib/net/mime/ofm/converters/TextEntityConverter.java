package my.lib.net.mime.ofm.converters;

import java.util.List;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.AbstractEntityConverter;
import my.lib.net.mime.ofm.EntityAcceptor;

public class TextEntityConverter extends AbstractEntityConverter {

	public TextEntityConverter(List<EntityAcceptor> acceptors) {
		super(acceptors);
	}

	@Override
	public Object convertEntity(BodyPart bodyPart) {
		return bodyPart.getEntity();
	}
}

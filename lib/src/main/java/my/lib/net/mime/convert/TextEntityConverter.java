package my.lib.net.mime.convert;

import java.util.List;

import my.lib.net.mime.BodyPart;

public class TextEntityConverter extends AbstractEntityConverter {

	public TextEntityConverter(List<EntityAcceptor> acceptors) {
		super(acceptors);
	}

	@Override
	public Object convert(BodyPart bodyPart) {
		return bodyPart.getEntity();
	}
}

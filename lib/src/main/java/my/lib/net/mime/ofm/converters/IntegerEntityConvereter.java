package my.lib.net.mime.ofm.converters;

import java.util.List;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.AbstractEntityConverter;
import my.lib.net.mime.ofm.EntityAcceptor;

public class IntegerEntityConvereter extends AbstractEntityConverter {

	public IntegerEntityConvereter(List<EntityAcceptor> acceptors) {
		super(acceptors);
	}

	@Override
	public Object convertEntity(BodyPart bodyPart) {
		return Integer.parseInt(bodyPart.getEntity());
	}
}

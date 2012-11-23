package my.lib.net.mime.ofm.converters;

import java.util.List;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.AbstractEntityConverter;
import my.lib.net.mime.ofm.EntityAcceptor;

public class DoubleEntityConverter extends AbstractEntityConverter {

		public DoubleEntityConverter(List<EntityAcceptor> acceptors) {
			super(acceptors);
		}

		@Override
		public Object convertEntity(BodyPart bodyPart) {
			return Double.parseDouble(bodyPart.getEntity());
		}
}

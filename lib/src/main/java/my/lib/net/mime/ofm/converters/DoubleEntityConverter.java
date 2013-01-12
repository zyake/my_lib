package my.lib.net.mime.ofm.converters;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.EntityConverter;

public class DoubleEntityConverter implements EntityConverter {

		@Override
		public Object convertEntity(BodyPart bodyPart) {
			return Double.parseDouble(bodyPart.getEntity());
		}
}

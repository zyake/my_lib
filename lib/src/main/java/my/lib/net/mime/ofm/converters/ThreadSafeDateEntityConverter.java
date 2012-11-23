package my.lib.net.mime.ofm.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.AbstractEntityConverter;
import my.lib.net.mime.ofm.EntityAcceptor;
import my.lib.net.mime.ofm.MIMEConvertException;

public class ThreadSafeDateEntityConverter extends AbstractEntityConverter {

	private ThreadLocal<DateFormat> format;

		public ThreadSafeDateEntityConverter(EntityAcceptor acceptor, final String formatText) {
			super(acceptor);
			this.format = new ThreadLocal<DateFormat>() {
				@Override
				protected DateFormat initialValue() {
					return new SimpleDateFormat(formatText);
				}
			};
		}

		@Override
		public Object convertEntity(BodyPart bodyPart) {
			try {
				return format.get().parseObject(bodyPart.getEntity());
			} catch (ParseException e) {
				throw new MIMEConvertException(e);
			}
		}
}

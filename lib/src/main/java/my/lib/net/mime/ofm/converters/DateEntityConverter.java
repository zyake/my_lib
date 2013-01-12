package my.lib.net.mime.ofm.converters;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.EntityConverter;
import my.lib.net.mime.ofm.MIMEConvertException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * スレッドセーフな日付フォーマッタの実装。
 */
public class DateEntityConverter implements EntityConverter {

	private ThreadLocal<DateFormat> format;

		public DateEntityConverter(final String formatText) {
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

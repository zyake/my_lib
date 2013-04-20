package my.lib.net.mime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.lib.util.Assert;
import my.lib.net.mime.ofm.MIMEConvertException;

public class MIMEUtil {

	private static Pattern DELIMITER_PATTERN = Pattern.compile("--[\\s\\w\\'\\)\\(\\+,\\-\\./:=\\?]+$");

	public static String MESSAGE_SEPARATOR = "\r\n";

	private MIMEUtil() {
	}

	public static boolean isMultipartDelimiter(String delimiter) {
		Assert.notNull(delimiter, "delimiter");

		Matcher matcher = DELIMITER_PATTERN.matcher(delimiter);
		boolean matched = matcher.matches();

		return matched;
	}

	public static String getDispositionName(BodyPart bodyPart) {
		String dispositionName = null;
		DISOSITION_SEARCH:
		for ( MIMEHeader header : bodyPart.getHeaders() ) {
			boolean targetFieldFound = "content-disposition".equals(header.getFieldName().toLowerCase());
			if ( !targetFieldFound ) {
				continue;
			}

			boolean isFormData = "form-data".equals(header.getFieldBody());
			if ( !isFormData ) {
				throw new MIMEConvertException("content-disposition is not form-data: " + header.getFieldBody());
			}

			for ( MIMEParam param : header.getParams() ) {
				boolean targetParamFound = "name".equals(param.getKey());
				if ( targetParamFound ) {
					dispositionName = param.getValue();
					break DISOSITION_SEARCH;
				}
			}
		}
		return dispositionName;
	}
}

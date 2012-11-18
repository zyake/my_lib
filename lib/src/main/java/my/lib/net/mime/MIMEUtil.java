package my.lib.net.mime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.lib.common.Assert;

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
}

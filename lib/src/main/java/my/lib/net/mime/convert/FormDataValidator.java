package my.lib.net.mime.convert;

import my.lib.net.mime.MultipartMessage;
import my.lib.net.mime.MultipartMessageValidator;

public class FormDataValidator implements MultipartMessageValidator {

	public boolean isValid(MultipartMessage msg) {
		return false;
	}

}

package my.lib.net.mime.ofm.validators;

import my.lib.net.mime.MultipartMessage;
import my.lib.net.mime.ofm.MultipartMessageValidator;

public class FormDataValidator implements MultipartMessageValidator {

	public boolean isValid(MultipartMessage msg) {
		return false;
	}
}

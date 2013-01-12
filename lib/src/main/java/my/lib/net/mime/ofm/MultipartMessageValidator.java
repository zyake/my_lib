package my.lib.net.mime.ofm;

import my.lib.net.mime.MultipartMessage;

public interface MultipartMessageValidator {

	boolean isValid(MultipartMessage msg);
}

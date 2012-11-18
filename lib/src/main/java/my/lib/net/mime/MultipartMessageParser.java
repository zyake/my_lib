package my.lib.net.mime;

public interface MultipartMessageParser {
	
	MIMEHeaderParser getHeaderParser();

	MultipartMessage parseMessage(String msg);
}

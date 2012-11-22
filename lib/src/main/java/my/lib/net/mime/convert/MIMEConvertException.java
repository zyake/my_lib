package my.lib.net.mime.convert;

public class MIMEConvertException extends RuntimeException {

	public MIMEConvertException(String msg) {
		super(msg);
	}
	
	public MIMEConvertException(Exception e) {
		super(e);
	}
}

package my.lib.common;

public class CommonException extends RuntimeException {

	public CommonException(String msg, Exception e) {
		super(msg, e);
	}
}

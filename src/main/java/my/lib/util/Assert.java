package my.lib.util;

public class Assert {

	private Assert() {
	}
	
	public static void notNull(Object obj, String msg) {
		if ( obj == null ) {
			throw new NullPointerException(msg);
		}
	}
}

package my.lib.net.mime;

public class MIMEParam {

	private String key;

	private String value;

	public MIMEParam(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}

package my.lib.net.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MIMEHeader {

	private String fieldName;

	private String fieldBody;

	private List<MIMEParam> params = new ArrayList<>();

	public MIMEHeader(String fieldName, String fieldBody, List<MIMEParam> params) {
		this.fieldName = fieldName;
		this.fieldBody = fieldBody;
		this.params.addAll(params);
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldBody() {
		return fieldBody;
	}

	public List<MIMEParam> getParams() {
		return Collections.unmodifiableList(params);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
			.append("{name=" + fieldName + ", body=" + fieldBody + ", params=" + params.toString() + "}");

		return builder.toString();
	}
}

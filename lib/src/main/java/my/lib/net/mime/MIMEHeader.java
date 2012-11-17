package my.lib.net.mime;

import java.util.Collections;
import java.util.List;

public class MIMEHeader {

	private String fieldName;
	
	private String fieldBody;
	
	private List<MIMEParam> params;
	
	public MIMEHeader(String fieldName, String fieldBody, List<MIMEParam> params) {
		this.fieldName = fieldName;
		this.fieldBody = fieldBody;
		this.params = params;
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
}

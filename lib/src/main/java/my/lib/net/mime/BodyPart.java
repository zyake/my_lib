package my.lib.net.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BodyPart {

	private List<MIMEHeader> headers = new ArrayList<MIMEHeader>();
	
	private String entity;
	
	public BodyPart(String entity, List<MIMEHeader> headers) {
		this.entity = entity;
		this.headers = headers;
	}
	
	public List<MIMEHeader> getHeaders() {
		return Collections.unmodifiableList(headers);
	}
	
	public String getEntity() {
		return entity;
	}
}

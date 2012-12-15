package my.lib.net.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BodyPart {

	private List<MIMEHeader> headers = new ArrayList<>();

	private String entity;

	public BodyPart(String entity, List<MIMEHeader> headers) {
		this.entity = entity;
		this.headers.addAll( headers);
	}

	public List<MIMEHeader> getHeaders() {
		return Collections.unmodifiableList(headers);
	}

	public String getEntity() {
		return entity;
	}

	@Override
	public String toString() {
		return "{entity=" + entity + ", headers=" + headers + "}";
	}
}

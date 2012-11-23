package my.lib.net.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipartMessage {

	private List<BodyPart> bodyParts = new ArrayList<>();

	public MultipartMessage(List<BodyPart> bodyParts) {
		this.bodyParts .addAll(bodyParts);
	}

	public List<BodyPart> getBodyParts() {
		return Collections.unmodifiableList(bodyParts);
	}
}

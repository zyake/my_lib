package my.lib.net.mime.ofm.acceptors;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEHeader;

public class ContentTypeAcceptor extends AbstractEntityAcceptor {

	private String contentType;

	public ContentTypeAcceptor(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public boolean accept(BodyPart bodyPart) {
		for ( MIMEHeader header : bodyPart.getHeaders() ) {
			boolean contentTypeMatched =
					"content-type".equals(header.getFieldName().toLowerCase()) &&
					contentType.equals(header.getFieldBody());

			if( contentTypeMatched ) {
				return true;
			}
		}

		return false;
	}
}

package my.lib.net.mime.convert.acceptors;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEHeader;
import my.lib.net.mime.convert.AbstractEntityAcceptor;

public class ContentTypeAcceptor extends AbstractEntityAcceptor {

	private String contentType;

	public ContentTypeAcceptor(String contentType) {
		this.contentType = contentType;
	}
	
	@Override
	public boolean accept(BodyPart bodyPart) {
		for ( MIMEHeader header : bodyPart.getHeaders() ) {
			boolean contentTypeMatched = 
					"Content-Type".equals(header.getFieldName()) &&
					contentType.equals(header.getFieldBody());

			if( contentTypeMatched ) {
				return true;
			}
		}
		
		return false;
	}
}

package my.lib.net.mime.convert;

import java.util.List;

import my.lib.net.mime.BodyPart;

public interface EntityConverter {

	Object convert(BodyPart bodyPart);
	
	boolean accept(BodyPart bodyPart);
	
	List<EntityAcceptor> getAcceptors();
}

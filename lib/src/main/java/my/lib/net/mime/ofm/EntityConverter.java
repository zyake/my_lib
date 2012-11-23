package my.lib.net.mime.ofm;

import java.util.List;

import my.lib.net.mime.BodyPart;

public interface EntityConverter {

	Object convertEntity(BodyPart bodyPart);
	
	boolean accept(BodyPart bodyPart);
	
	List<EntityAcceptor> getAcceptors();
}

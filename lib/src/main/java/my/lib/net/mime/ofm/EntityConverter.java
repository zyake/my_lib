package my.lib.net.mime.ofm;

import my.lib.net.mime.BodyPart;

public interface EntityConverter {

	Object convertEntity(BodyPart bodyPart);

	boolean accept(BodyPart bodyPart);

	EntityAcceptor getAcceptor();
}

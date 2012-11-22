package my.lib.net.mime.convert;

import my.lib.net.mime.BodyPart;

public interface EntityAcceptor {

	boolean accept(BodyPart bodyPart);
	
	EntityAcceptor and(EntityAcceptor acceptor);
	
	EntityAcceptor or(EntityAcceptor acceptor);

	EntityAcceptor xor(EntityAcceptor acceptor);
	
	EntityAcceptor not(EntityAcceptor acceptor);
}

package my.lib.net.mime.ofm.acceptors;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.EntityAcceptor;

public abstract class AbstractEntityAcceptor implements EntityAcceptor {

	public abstract boolean accept(BodyPart bodyPart);

	public EntityAcceptor and(EntityAcceptor acceptor) {
		return new AndAcceptor(this, acceptor);
	}

	public EntityAcceptor or(EntityAcceptor acceptor) {
		return new OrAcceptor(this, acceptor);
	}

	public EntityAcceptor xor(EntityAcceptor acceptor) {
		return new XOrAcceptor(this, acceptor);
	}

	public EntityAcceptor not(EntityAcceptor acceptor) {
		return null;
	}
}

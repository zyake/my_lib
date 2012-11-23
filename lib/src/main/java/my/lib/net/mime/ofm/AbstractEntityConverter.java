package my.lib.net.mime.ofm;

import my.lib.net.mime.BodyPart;

public abstract class AbstractEntityConverter implements EntityConverter {

	private EntityAcceptor acceptor;

	public AbstractEntityConverter(EntityAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	public abstract Object convertEntity(BodyPart bodyPart);

	public boolean accept(BodyPart bodyPart) {
		boolean accepted = acceptor.accept(bodyPart);

		return accepted;
	}

	public EntityAcceptor getAcceptor() {
		return acceptor;
	}
}

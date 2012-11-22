package my.lib.net.mime.convert.acceptors;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.convert.AbstractEntityAcceptor;
import my.lib.net.mime.convert.EntityAcceptor;

public class NotAcceptor extends AbstractEntityAcceptor {
	
	private EntityAcceptor acceptor;
	
	public NotAcceptor(EntityAcceptor acceptor) {
		this.acceptor = acceptor;
	}
	
	public boolean accept(BodyPart bodyPart) {
		return !acceptor.accept(bodyPart);
	}
}

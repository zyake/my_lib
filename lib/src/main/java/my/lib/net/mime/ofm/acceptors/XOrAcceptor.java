package my.lib.net.mime.ofm.acceptors;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.EntityAcceptor;

public class XOrAcceptor extends AbstractEntityAcceptor {

	private EntityAcceptor left;
	
	private EntityAcceptor right;

	public XOrAcceptor(EntityAcceptor left, EntityAcceptor right) {
		this.left = left;
		this.right = right;
	}
	
	public boolean accept(BodyPart bodyPart) {
		return left.accept(bodyPart) ^ right.accept(bodyPart);
	}
}

package my.lib.net.mime.ofm.acceptors;

import my.lib.net.mime.BodyPart;

public class AllAcceptor extends AbstractEntityAcceptor {

	@Override
	public boolean accept(BodyPart bodyPart) {
		return true;
	}
}

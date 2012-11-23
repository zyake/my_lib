package my.lib.net.mime.ofm.acceptors;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.AbstractEntityAcceptor;

public class AllAcceptor extends AbstractEntityAcceptor {

	@Override
	public boolean accept(BodyPart bodyPart) {
		return true;
	}
}

package my.lib.net.mime.convert.acceptors;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.convert.AbstractEntityAcceptor;

public class NullAcceptor extends AbstractEntityAcceptor {

	@Override
	public boolean accept(BodyPart bodyPart) {
		return true;
	}
}

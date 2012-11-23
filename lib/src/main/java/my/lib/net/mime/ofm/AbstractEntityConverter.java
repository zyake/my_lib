package my.lib.net.mime.ofm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import my.lib.net.mime.BodyPart;

public abstract class AbstractEntityConverter implements EntityConverter {

	private List<EntityAcceptor> acceptors = new ArrayList<EntityAcceptor>();

	public AbstractEntityConverter(List<EntityAcceptor> acceptors) {
		this.acceptors.addAll(acceptors);
	}

	public abstract Object convertEntity(BodyPart bodyPart);

	public boolean accept(BodyPart bodyPart) {
		for ( EntityAcceptor acceptor : acceptors ) {
			boolean accepted = acceptor.accept(bodyPart);
			if ( accepted ) {
				return true;
			}
		}

		return false;
	}

	public List<EntityAcceptor> getAcceptors() {
		return Collections.unmodifiableList(acceptors);
	}
}

package my.lib.net.mime.convert;

import java.util.Collections;
import java.util.List;

import my.lib.net.mime.BodyPart;

public abstract class AbstractEntityConverter implements EntityConverter {

	private List<EntityAcceptor> acceptors;
	
	public AbstractEntityConverter(List<EntityAcceptor> acceptors) {
		this.acceptors = acceptors;
	}
	
	public abstract Object convert(BodyPart bodyPart);

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

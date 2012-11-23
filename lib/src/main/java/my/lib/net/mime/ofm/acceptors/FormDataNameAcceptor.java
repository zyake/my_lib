package my.lib.net.mime.ofm.acceptors;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEUtil;
import my.lib.net.mime.ofm.AbstractEntityAcceptor;

public class FormDataNameAcceptor extends AbstractEntityAcceptor {

	private String dispositionName;

	public FormDataNameAcceptor(String dispositionName) {
		this.dispositionName = dispositionName;
	}

	@Override
	public boolean accept(BodyPart bodyPart) {
		String dispositionName = MIMEUtil.getDispositionName(bodyPart);
		boolean isTargetName = this.dispositionName.equals(dispositionName);

		return isTargetName;
	}

}

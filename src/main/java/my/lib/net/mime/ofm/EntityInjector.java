package my.lib.net.mime.ofm;

import my.lib.net.mime.BodyPart;

public interface EntityInjector {

	void inject(BodyPart body, Object entity, Object target);
}

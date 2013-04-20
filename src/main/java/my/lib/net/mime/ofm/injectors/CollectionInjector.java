package my.lib.net.mime.ofm.injectors;

import java.util.Collection;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.ofm.EntityInjector;
import my.lib.net.mime.ofm.MIMEConvertException;

/**
 * ボディーパートのエンティティをコレクションの最後尾に追加するインジェクタ。
 * @author zyake
 */
public class CollectionInjector implements EntityInjector {

	public void inject(BodyPart body, Object entity, Object target) {
		boolean isCollection = target instanceof Collection;
		if ( !isCollection ) {
			throw new MIMEConvertException("target is not collection: " + target.getClass().getCanonicalName());
		}

		Collection collection = ( Collection )  target;
		collection.add(entity);
	}
}

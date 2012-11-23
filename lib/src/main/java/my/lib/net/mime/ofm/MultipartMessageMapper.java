package my.lib.net.mime.ofm;

import java.util.List;

import my.lib.net.mime.MultipartMessage;

public interface MultipartMessageMapper {

	void mapToObject(MultipartMessage msg, Object target);

	EntityInjector getInjector();

	List<EntityConverter> getConverters();
}

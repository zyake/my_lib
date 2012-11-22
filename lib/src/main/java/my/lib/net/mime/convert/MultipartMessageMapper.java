package my.lib.net.mime.convert;

import java.util.List;

import my.lib.net.mime.MultipartMessage;

public interface MultipartMessageMapper {

	void mapToObject(MultipartMessage msg, Object target);
	
	List<EntityConverter> getConverters();
	
	List<FieldMatcher> getMatchers();
}

package my.lib.net.mime.ofm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEHeader;
import my.lib.net.mime.MIMEParam;
import my.lib.net.mime.MultipartMessage;
import my.lib.net.mime.ofm.acceptors.AllAcceptor;
import my.lib.net.mime.ofm.converters.TextEntityConverter;
import my.lib.net.mime.ofm.injectors.FormDataFieldInjector;

import org.junit.Test;

public class DefaultMultipartMessageMapperTest {

	private class MyForm {

		private String name;

		private String address;

		private String birthday;
	}

	@Test
	public void testMapToObject01() {
		// initialize
		List<EntityAcceptor> acceptors = new ArrayList<EntityAcceptor>();
		acceptors.add(new AllAcceptor());

		List<EntityConverter> converters = new ArrayList<EntityConverter>();
		converters.add(new TextEntityConverter(acceptors));

		DefaultMultipartMessageMapper target = new DefaultMultipartMessageMapper(
				converters, new FormDataFieldInjector());

		// test
		List<BodyPart> bodyParts = new ArrayList<BodyPart>();
		{
			List<MIMEParam> params = new ArrayList<MIMEParam>();
			params.add(new MIMEParam("name", "name"));
			List<MIMEHeader> headers = new ArrayList<MIMEHeader>();
			headers.add(new MIMEHeader("Content-Disposition",  "form-data", params));

			bodyParts.add(new BodyPart("name1", headers));
		}
		{
			List<MIMEParam> params = new ArrayList<MIMEParam>();
			params.add(new MIMEParam("name", "address"));
			List<MIMEHeader> headers = new ArrayList<MIMEHeader>();
			headers.add(new MIMEHeader("Content-Disposition",  "form-data", params));

			bodyParts.add(new BodyPart("address1", headers));
		}
		{
			List<MIMEParam> params = new ArrayList<MIMEParam>();
			params.add(new MIMEParam("name", "birthday"));
			List<MIMEHeader> headers = new ArrayList<MIMEHeader>();
			headers.add(new MIMEHeader("Content-Disposition",  "form-data", params));

			bodyParts.add(new BodyPart("birthday1", headers));
		}

		MultipartMessage multipartMessage = new MultipartMessage(bodyParts);

		// tests
		MyForm myForm = new MyForm();
		target.mapToObject(multipartMessage, myForm);

		// assert
		assertEquals("name1", myForm.name);
		assertEquals("address1", myForm.address);
		assertEquals("birthday1", myForm.birthday);
	}

}

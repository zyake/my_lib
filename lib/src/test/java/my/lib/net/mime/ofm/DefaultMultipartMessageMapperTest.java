package my.lib.net.mime.ofm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEHeader;
import my.lib.net.mime.MIMEParam;
import my.lib.net.mime.MultipartMessage;
import my.lib.net.mime.ofm.acceptors.FormDataNameAcceptor;
import my.lib.net.mime.ofm.converters.TextEntityConverter;
import my.lib.net.mime.ofm.converters.ThreadSafeDateEntityConverter;
import my.lib.net.mime.ofm.injectors.FormDataFieldInjector;

import org.junit.Test;

public class DefaultMultipartMessageMapperTest {

	private class MyForm {

		private String name;

		private String address;

		private Date birthday;
	}

	@Test
	public void testMapToObject01() {
		// initialize
		List<EntityConverter> converters = new ArrayList<>();
		{
			EntityAcceptor acceptor = new FormDataNameAcceptor("name").or(new FormDataNameAcceptor("address"));
			converters.add(new TextEntityConverter(acceptor));
		}
		{
			converters.add(new ThreadSafeDateEntityConverter(
					new FormDataNameAcceptor("birthday"), "yyyyMMdd"));
		}
		DefaultMultipartMessageMapper target = new DefaultMultipartMessageMapper(
				converters, new FormDataFieldInjector());

		// test
		List<BodyPart> bodyParts = new ArrayList<>();
		{
			List<MIMEParam> params = new ArrayList<>();
			params.add(new MIMEParam("name", "name"));
			List<MIMEHeader> headers = new ArrayList<>();
			headers.add(new MIMEHeader("Content-Disposition",  "form-data", params));

			bodyParts.add(new BodyPart("name1", headers));
		}
		{
			List<MIMEParam> params = new ArrayList<>();
			params.add(new MIMEParam("name", "address"));
			List<MIMEHeader> headers = new ArrayList<>();
			headers.add(new MIMEHeader("Content-Disposition",  "form-data", params));

			bodyParts.add(new BodyPart("address1", headers));
		}
		{
			List<MIMEParam> params = new ArrayList<>();
			params.add(new MIMEParam("name", "birthday"));
			List<MIMEHeader> headers = new ArrayList<>();
			headers.add(new MIMEHeader("Content-Disposition",  "form-data", params));

			bodyParts.add(new BodyPart("19870205", headers));
		}

		MultipartMessage multipartMessage = new MultipartMessage(bodyParts);

		// tests
		MyForm myForm = new MyForm();
		target.mapToObject(multipartMessage, myForm);

		// assert
		assertEquals("name1", myForm.name);
		assertEquals("address1", myForm.address);
		assertEquals("Thu Feb 05 00:00:00 JST 1987", myForm.birthday.toString());
	}

}

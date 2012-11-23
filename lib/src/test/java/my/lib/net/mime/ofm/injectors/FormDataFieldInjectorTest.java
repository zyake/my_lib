package my.lib.net.mime.ofm.injectors;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEHeader;
import my.lib.net.mime.MIMEParam;
import my.lib.net.mime.ofm.MIMEConvertException;

import org.junit.Test;

public class FormDataFieldInjectorTest {

	private static class MyForm {

		private String name;

		public String getName() {
			return name;
		}
	}

	@Test
	public void testInject01() throws Exception {
		// initialize
		List<MIMEParam> params = new ArrayList<>();
		Collections.addAll(params,
				new MIMEParam("name", "name"));

		List<MIMEHeader> headers = new ArrayList<>();
		Collections.addAll(headers,
				new MIMEHeader("content-disposition", "form-data", params));

		BodyPart bodyPart = new BodyPart("test", headers);

		FormDataFieldInjector target = new FormDataFieldInjector();

		// test
		MyForm myForm = new MyForm();
		 target.inject(bodyPart, "entity", myForm);

		// assert
		assertEquals("entity", myForm.getName());
	}

	/**
	 * field not found
	 */
	@Test(expected=MIMEConvertException.class)
	public void testInject02() throws Exception {
		// initialize
		List<MIMEParam> params = new ArrayList<>();
		Collections.addAll(params,
				new MIMEParam("name", "piyo"));

		List<MIMEHeader> headers = new ArrayList<>();
		Collections.addAll(headers,
				new MIMEHeader("content-disposition", "form-data", params));

		BodyPart bodyPart = new BodyPart("test", headers);

		FormDataFieldInjector target = new FormDataFieldInjector();

		// test
		MyForm myForm = new MyForm();
		 target.inject(bodyPart, "entity", myForm);
	}

	/**
	 *  is not form-data
	 */
	@Test(expected=MIMEConvertException.class)
	public void testInject03() throws Exception {
		// initialize
		List<MIMEParam> params = new ArrayList<>();
		Collections.addAll(params,
				new MIMEParam("name", "name"));

		List<MIMEHeader> headers = new ArrayList<>();
		Collections.addAll(headers,
				new MIMEHeader("content-disposition", "form-data?", params));

		BodyPart bodyPart = new BodyPart("test", headers);

		FormDataFieldInjector target = new FormDataFieldInjector();

		// test
		MyForm myForm = new MyForm();
		 target.inject(bodyPart, "entity", myForm);
	}

	/**
	 * ignore case
	 */
	@Test
	public void testInject04() throws Exception {
		// initialize
		List<MIMEParam> params = new ArrayList<>();
		Collections.addAll(params,
				new MIMEParam("name", "name"));

		List<MIMEHeader> headers = new ArrayList<>();
		Collections.addAll(headers,
				new MIMEHeader("Content-Disposition", "form-data", params));

		BodyPart bodyPart = new BodyPart("test", headers);

		FormDataFieldInjector target = new FormDataFieldInjector();

		// test
		MyForm myForm = new MyForm();
		 target.inject(bodyPart, "entity", myForm);
	}

}

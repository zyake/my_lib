package my.lib.net.mime.ofm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import my.lib.net.mime.BodyPart;
import my.lib.net.mime.MIMEHeader;
import my.lib.net.mime.MIMEParam;
import my.lib.net.mime.MultipartMessage;

import org.junit.Test;

public class DefaultMultipartMessageMapperTest {

	private class MyForm {

		private String name;

		private String address;

		private Date birthday;

        @Override
        public String toString() {
            return "name=" + name + ", address=" + address + ", birthday=" + birthday;
        }
    }

	@Test
	public void testMapToObject01() {
		// initialize
        MultipartMessageMapper target = new MapperBuilder()
                .addHolder(Converters.TEXT, "name")
                .addHolder(Converters.TEXT, "address")
                .addDateHolder("birthday", "yyyyMMdd")
                .setInjector(Injectors.FIELD)
                .build();

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

			bodyParts.add(new BodyPart("19870205", headers));
		}

		MultipartMessage multipartMessage = new MultipartMessage(bodyParts);

		// tests
		MyForm myForm = new MyForm();
		target.mapToObject(multipartMessage, myForm);

		// assert
        assertThat(myForm.toString(), is("name=name1, address=address1, birthday=Thu Feb 05 00:00:00 JST 1987"));
	}

}

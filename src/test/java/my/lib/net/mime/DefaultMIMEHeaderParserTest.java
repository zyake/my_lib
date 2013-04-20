package my.lib.net.mime;

import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class DefaultMIMEHeaderParserTest {

	@Test
	public void testParseHeader01_normal() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();

		MIMEHeader header = target.parseHeader("content-type: text/plain");

		MIMEHeader expected = new MIMEHeader("content-type", "text/plain", new ArrayList<MIMEParam>());

		assertThat(header.toString(), is(expected.toString()));
	}

	@Test
	public void testParseHeader02_normal_hasEndSemicoron() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();

		MIMEHeader header = target.parseHeader("content-type: text/plain;");

		MIMEHeader expected = new MIMEHeader("content-type", "text/plain", new ArrayList<MIMEParam>());

		assertThat(header.toString(), is(expected.toString()));
	}

	@Test
	public void testParseHeader03_normal_hasFieldBodySpaceAndSemicoron() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();

		MIMEHeader header = target.parseHeader("content-type: text/plain     ;");

		MIMEHeader expected = new MIMEHeader("content-type", "text/plain", new ArrayList<MIMEParam>());

		assertThat(header.toString(), is(expected.toString()));
	}

	@Test
	public void testParseHeader04_normal_hasOneParam() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();

		MIMEHeader header = target.parseHeader("content-type: text/plain;charset=windows-1250");

		MIMEHeader expected = new MIMEHeader("content-type", "text/plain",
				asList(new MIMEParam("charset", "windows-1250"))
		);

		assertThat(header.toString(), is(expected.toString()));
	}

	@Test
	public void testParseHeader05_normal_hasOneEscapedParam() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();

		MIMEHeader header = target.parseHeader("Content-Type: multipart/mixed; boundary=\"inner\"");

		MIMEHeader expected = new MIMEHeader("Content-Type", "multipart/mixed",
				asList(new MIMEParam("boundary", "inner"))
		);

		assertThat(header.toString(), is(expected.toString()));
	}

	@Test
	public void testParseHeader06_hasMultipleParams() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();

		MIMEHeader header = target.parseHeader("Content-Type: multipart/mixed; boundary=\"inner\";charset=UTF-8");

		MIMEHeader expected = new MIMEHeader("Content-Type", "multipart/mixed",
			asList(
				new MIMEParam("boundary", "inner"),
				new MIMEParam("charset", "UTF-8")
			)
		);

		assertThat(header.toString(), is(expected.toString()));
	}
}

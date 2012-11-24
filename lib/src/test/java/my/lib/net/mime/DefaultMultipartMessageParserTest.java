package my.lib.net.mime;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.junit.Test;

public class DefaultMultipartMessageParserTest {

	@Test
	public void testParseMessage01() {
		String multipartMessage =
				"--boundary42\r\n" +
				"Content-Type: text/plain; charset=us-ascii\r\n" +
				"\r\n" +
				"... plain text version of message goes here ...\r\n" +
				"--boundary42--";

		DefaultMultipartMessageParser target = new DefaultMultipartMessageParser();

		MultipartMessage parsedMessage = target.parseMessage(multipartMessage);

		// start assertion
		assertEquals(1, parsedMessage.getBodyParts().size());

		BodyPart bodyPart = parsedMessage.getBodyParts().get(0);
		assertEquals("... plain text version of message goes here ...", bodyPart.getEntity());
		assertEquals(1, bodyPart.getHeaders().size());

		MIMEHeader header = bodyPart.getHeaders().get(0);
		assertEquals("Content-Type", header.getFieldName());
		assertEquals("text/plain", header.getFieldBody());

		assertEquals(1, header.getParams().size());
		MIMEParam param = header.getParams().get(0);
		assertEquals("charset", param.getKey());
		assertEquals("us-ascii", param.getValue());
	}

	@Test
	public void testParseMessage02() {
		String multipartMessage =
				"--boundary42\r\n" +
				"\r\n" +
				"... plain text version of message goes here ...\r\n" +
				"--boundary42--";

		DefaultMultipartMessageParser target = new DefaultMultipartMessageParser();

		MultipartMessage parsedMessage = target.parseMessage(multipartMessage);

		// start assertion
		assertEquals(1, parsedMessage.getBodyParts().size());

		BodyPart bodyPart = parsedMessage.getBodyParts().get(0);
		assertEquals("... plain text version of message goes here ...", bodyPart.getEntity());
		assertEquals(0, bodyPart.getHeaders().size());
	}


	@Test
	public void testParseMessage03() {
		String multipartMessage =
				"--boundary42\r\n" +
				"\r\n" +
				"... plain text version of message goes here ...\r\n";

		DefaultMultipartMessageParser target = new DefaultMultipartMessageParser();

		MultipartMessage parsedMessage = target.parseMessage(multipartMessage);

		// start assertion
		assertEquals(1, parsedMessage.getBodyParts().size());

		BodyPart bodyPart = parsedMessage.getBodyParts().get(0);
		assertEquals("... plain text version of message goes here ...", bodyPart.getEntity());
		assertEquals(0, bodyPart.getHeaders().size());
	}

	@Test
	public void testParseMessage04() {
		String multipartMessage =
				"--boundary42\r\n" +
				"\r\n" +
				"... plain text version of message goes here ...\r\n" +
				"\r\n" +
				"--boundary42\r\n" +
				"Content-Type: text/enriched\r\n" +
				"\r\n" +
				"... RFC 1896 text/enriched version of same message\r\n" +
				"    goes here ...\r\n" +
				"\r\n" +
				"--boundary42--";

		DefaultMultipartMessageParser target = new DefaultMultipartMessageParser();

		MultipartMessage parsedMessage = target.parseMessage(multipartMessage);

		// start assertion
		assertEquals(2, parsedMessage.getBodyParts().size());

		{
			BodyPart bodyPart = parsedMessage.getBodyParts().get(0);
			assertEquals("... plain text version of message goes here ...\r\n", bodyPart.getEntity());
			assertEquals(0, bodyPart.getHeaders().size());
		}
		{
			BodyPart bodyPart = parsedMessage.getBodyParts().get(1);
			assertEquals(
					"... RFC 1896 text/enriched version of same message\r\n    goes here ...\r\n",
					bodyPart.getEntity());
			assertEquals(1, bodyPart.getHeaders().size());

			MIMEHeader header = bodyPart.getHeaders().get(0);
			assertEquals("Content-Type", header.getFieldName());
			assertEquals("text/enriched", header.getFieldBody());

			assertEquals(0, header.getParams().size());
		}
	}

	@Test
	public void testParseMessage05() {
		String multipartMessage =
				"--boundary42\r\n" +
				"Content-Type: text/plain; \r\n charset=us-ascii\r\n" +
				"\r\n" +
				"... plain text version of message goes here ...\r\n" +
				"--boundary42--";

		DefaultMultipartMessageParser target = new DefaultMultipartMessageParser();

		MultipartMessage parsedMessage = target.parseMessage(multipartMessage);

		// start assertion
		assertEquals(1, parsedMessage.getBodyParts().size());

		BodyPart bodyPart = parsedMessage.getBodyParts().get(0);
		assertEquals("... plain text version of message goes here ...", bodyPart.getEntity());
		assertEquals(1, bodyPart.getHeaders().size());

		MIMEHeader header = bodyPart.getHeaders().get(0);
		assertEquals("Content-Type", header.getFieldName());
		assertEquals("text/plain", header.getFieldBody());

		assertEquals(1, header.getParams().size());
		MIMEParam param = header.getParams().get(0);
		assertEquals("charset", param.getKey());
		assertEquals("us-ascii", param.getValue());
	}

	@Test
	public void testParseMessage06() {
		String multipartMessage =
				"--boundary42\r\n" +
				"Content-Type: text/plain; \r\n charset=us-ascii\r\n" +
				"Host: hoge\r\n piyo\r\n" +
				"\r\n" +
				"... plain text version of message goes here ...\r\n" +
				"--boundary42--";

		DefaultMultipartMessageParser target = new DefaultMultipartMessageParser();

		MultipartMessage parsedMessage = target.parseMessage(multipartMessage);

		// start assertion
		assertEquals(1, parsedMessage.getBodyParts().size());

		BodyPart bodyPart = parsedMessage.getBodyParts().get(0);
		assertEquals("... plain text version of message goes here ...", bodyPart.getEntity());
		assertEquals(2, bodyPart.getHeaders().size());
		{
			MIMEHeader header = bodyPart.getHeaders().get(0);
			assertEquals("Content-Type", header.getFieldName());
			assertEquals("text/plain", header.getFieldBody());

			assertEquals(1, header.getParams().size());
			MIMEParam param = header.getParams().get(0);
			assertEquals("charset", param.getKey());
			assertEquals("us-ascii", param.getValue());
		}
		{
			MIMEHeader header = bodyPart.getHeaders().get(1);
			assertEquals("Host", header.getFieldName());
			assertEquals("hogepiyo", header.getFieldBody());
		}
	}

	/**
	 * 実際にGoogle Chromeから送信したmultipart/form-data形式データバイト列を読み込む
	 */
	@Test
	public void testParseMessage07() throws Exception {
		File file = new File("src/test/java/my/lib/net/mime/multipart_message.bin");
		FileInputStream inputStream = new FileInputStream(file);

		ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
		FileChannel channel = inputStream.getChannel();
		channel.read(buffer);
		buffer.position(0);

		Charset charset = Charset.forName("UTF-8");
		CharBuffer charBuffer = charset.decode(buffer);
		charBuffer.position(0);
		String msg = charBuffer.toString();

		DefaultMultipartMessageParser target = new DefaultMultipartMessageParser();
		MultipartMessage multipartMessage = target.parseMessage(msg);

		assertEquals(2, multipartMessage.getBodyParts().size());
		{
			BodyPart bodyPart = multipartMessage.getBodyParts().get(0);
			assertEquals("日本語でおｋ？\r\nてすとほげほげ", bodyPart.getEntity());
			assertEquals(1, bodyPart.getHeaders().size());

			MIMEHeader header = bodyPart.getHeaders().get(0);
			assertEquals("Content-Disposition", header.getFieldName());
			assertEquals("form-data", header.getFieldBody());

			assertEquals(1, header.getParams().size());
			MIMEParam param = header.getParams().get(0);
			assertEquals("name", param.getKey());
			assertEquals("test1", param.getValue());
		}
		{
			BodyPart bodyPart = multipartMessage.getBodyParts().get(1);
			assertEquals("testdata\r\ntestdata", bodyPart.getEntity());
			assertEquals(1, bodyPart.getHeaders().size());

			MIMEHeader header = bodyPart.getHeaders().get(0);
			assertEquals("Content-Disposition", header.getFieldName());
			assertEquals("form-data", header.getFieldBody());

			assertEquals(1, header.getParams().size());
			MIMEParam param = header.getParams().get(0);
			assertEquals("name", param.getKey());
			assertEquals("test2", param.getValue());
		}
	}
}

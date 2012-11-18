package my.lib.net.mime;

import static org.junit.Assert.*;

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
}

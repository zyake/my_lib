package my.lib.net.mime;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultMIMEHeaderParserTest {

	@Test
	public void testParseHeader01() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();
		
		MIMEHeader header = target.parseHeader("content-type: text/plain");
		
		assertEquals("content-type", header.getFieldName());
		assertEquals("text/plain", header.getFieldBody());
		assertEquals(0, header.getParams().size());
	}
	
	@Test
	public void testParseHeader02() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();
		
		MIMEHeader header = target.parseHeader("content-type: text/plain;");
		
		assertEquals("content-type", header.getFieldName());
		assertEquals("text/plain", header.getFieldBody());
		assertEquals(0, header.getParams().size());
	}
	
	@Test
	public void testParseHeader03() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();
		
		MIMEHeader header = target.parseHeader("content-type: text/plain     ;");
		
		assertEquals("content-type", header.getFieldName());
		assertEquals("text/plain", header.getFieldBody());
		assertEquals(0, header.getParams().size());
	}
	
	@Test
	public void testParseHeader04() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();
		
		MIMEHeader header = target.parseHeader("content-type: text/plain;charset=windows-1250");
		
		assertEquals("content-type", header.getFieldName());
		assertEquals("text/plain", header.getFieldBody());
		assertEquals(1, header.getParams().size());
		{
			MIMEParam param = header.getParams().get(0);
			assertEquals("charset", param.getKey());
			assertEquals("windows-1250", param.getValue());
		}
	}
	
	@Test
	public void testParseHeader05() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();
		
		MIMEHeader header = target.parseHeader("Content-Type: multipart/mixed; boundary=\"inner\"");
		
		assertEquals("Content-Type", header.getFieldName());
		assertEquals("multipart/mixed", header.getFieldBody());
		assertEquals(1, header.getParams().size());
		{
			MIMEParam param = header.getParams().get(0);
			assertEquals("boundary", param.getKey());
			assertEquals("inner", param.getValue());
		}
	}
	
	@Test
	public void testParseHeader06() {
		MIMEHeaderParser target = new DefaultMIMEHeaderParser();
		
		MIMEHeader header = target.parseHeader("Content-Type: multipart/mixed; boundary=\"inner\";charset=UTF-8");
		
		assertEquals("Content-Type", header.getFieldName());
		assertEquals("multipart/mixed", header.getFieldBody());
		assertEquals(2, header.getParams().size());
		{
			MIMEParam param = header.getParams().get(0);
			assertEquals("boundary", param.getKey());
			assertEquals("inner", param.getValue());
		}
		{
			MIMEParam param = header.getParams().get(1);
			assertEquals("charset", param.getKey());
			assertEquals("UTF-8", param.getValue());
		}
	}
}

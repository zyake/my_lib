package my.lib.net.mime;

import static org.junit.Assert.*;

import org.junit.Test;

public class MIMEUtilTest {

	@Test
	public void testIsDelimiter01() {
		boolean isDelimiter = MIMEUtil.isMultipartDelimiter("--gc0pJq0M:08jU534c0p");
		assertTrue(isDelimiter);
	}
	
	@Test
	public void testIsDelimiter02() {
		boolean isDelimiter = MIMEUtil.isMultipartDelimiter("--gc0pJq0M:08jU534c0p--");
		assertTrue(isDelimiter);
	}
	
	@Test
	public void testIsDelimiter03() {
		boolean isDelimiter = MIMEUtil.isMultipartDelimiter("--     gc0pJq0M:08jU534c0p--");
		assertTrue(isDelimiter);
	}
	
	@Test
	public void testIsDelimiter04() {
		boolean isDelimiter = MIMEUtil.isMultipartDelimiter("--gc0pJq0M:08jU534c&0p");
		assertFalse(isDelimiter);
	}
}

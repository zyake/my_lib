package my.lib.net.mime;

import java.util.ArrayList;
import java.util.List;

import my.lib.common.Assert;

public class DefaultMultipartMessageParser implements MultipartMessageParser {

	private MIMEHeaderParser headerParser = new DefaultMIMEHeaderParser();
	
	private enum ParserStates {
		OnStart,
		OnHeader,
		OnBody
	}

	public DefaultMultipartMessageParser setHeaderParser(MIMEHeaderParser parser) {
		Assert.notNull(parser, "parser");
		
		this.headerParser = parser;
		
		return this;
	}
	
	public MIMEHeaderParser getHeaderParser() {
		return headerParser;
	}
	
	public MultipartMessage parseMessage(String msg) {
		Assert.notNull(msg, "msg");
		
		List<MIMEHeader> headers = new ArrayList<MIMEHeader>();
		StringBuilder headerLine = new StringBuilder();
		
		List<BodyPart> bodyParts = new ArrayList<BodyPart>();
		StringBuilder entityLines = new StringBuilder();
		
		ParserStates state = ParserStates.OnStart;
		String delimiter = null;
		String[] lines = msg.split(MIMEUtil.MESSAGE_SEPARATOR);
		for ( int i = 0 ; i < lines.length; i ++ ) {
			String currentLine = lines[i];
			switch ( state ) {
				case OnStart:
					boolean isDelimiter = MIMEUtil.isMultipartDelimiter(currentLine);
					if ( !isDelimiter ) {
						throw new MIMEParserException(
									"invalid delimiter on first line: " + currentLine);
					}
					delimiter = currentLine;
					state = ParserStates.OnHeader;
					break;
				
				case OnHeader:
					boolean isFolded = currentLine.startsWith(" ");
					if ( isFolded ) {
						headerLine.append(currentLine.trim());
						continue;
					}
					
					boolean isEndHeader = "".equals(currentLine);
					if ( isEndHeader ) {
						state = ParserStates.OnBody;
					}

					headerLine.append(currentLine);
					
					boolean existsHeader = headerLine.length() > 0;
					if( existsHeader ) {
						MIMEHeader header = headerParser.parseHeader(headerLine.toString());
						headers.add(header);
						headerLine = new StringBuilder();
					}
					
					break;
				
				case OnBody:
					boolean foundNextPart = delimiter.equals(currentLine);
					boolean foundEndPart = 
								currentLine.startsWith(delimiter) && 
								currentLine.endsWith("--") && 
								currentLine.length() == delimiter.length() + 2;
					if ( foundNextPart || foundEndPart ) {
						addNewBodyPart(headers, bodyParts, entityLines);
						
						headers = new ArrayList<MIMEHeader>();
						entityLines = new StringBuilder();
						state = ParserStates.OnHeader;
						
						continue;
					}

					entityLines.append(currentLine + MIMEUtil.MESSAGE_SEPARATOR);
					break;
			}
		}
		
		boolean reachEndWithoutEpilogueDelimiter = entityLines.length() > 1;
		if ( reachEndWithoutEpilogueDelimiter ) {
			addNewBodyPart(headers, bodyParts, entityLines);
		}
		
		return new MultipartMessage(bodyParts);
	}

	private void addNewBodyPart(List<MIMEHeader> headers,
		List<BodyPart> bodyParts, StringBuilder entityLines) {
		entityLines
		.deleteCharAt(entityLines.length() - 1)
		.deleteCharAt(entityLines.length() - 1);
		BodyPart bodyPart = new BodyPart(entityLines.toString(), headers);
		bodyParts.add(bodyPart);
	}
}

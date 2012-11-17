package my.lib.net.mime;

import java.util.ArrayList;
import java.util.List;

public class DefaultMIMEHeaderParser implements MIMEHeaderParser {

	private enum ParseStates {
		SearchFieldName,
		SearchFieldBody,
		SearchParamName,
		SearchParamBody
	}
	
	public MIMEHeader parseHeader(String header) {
		ParseStates state = ParseStates.SearchFieldName;
		StringBuilder fieldName = new StringBuilder();
		StringBuilder fieldBody = new StringBuilder();
		StringBuilder paramName = new StringBuilder();
		StringBuilder paramBody = new StringBuilder();
		List<MIMEParam> params = new ArrayList<MIMEParam>();
		
		for ( int i = 0 ; i < header.length() ; i ++ ) {
			char currentChar = header.charAt(i);
			
			switch ( state ) {
				case SearchFieldName:
					boolean isEndName = ':' == currentChar;
					if ( isEndName ) {
						state = ParseStates.SearchFieldBody;
						continue;
					}
					fieldName.append(currentChar);
				break;
				
				case SearchFieldBody:
					boolean isEndBody = ';' == currentChar; 
					if ( isEndBody ) {
						state = ParseStates.SearchParamName;
						continue;
					}
					fieldBody.append(currentChar);
					break;
				
				case SearchParamName:
					boolean isEndParamName = '=' == currentChar;
					if ( isEndParamName ) {
						state = ParseStates.SearchParamBody;
						continue;
					}
					paramName.append(currentChar);
					break;
				
				case SearchParamBody:
					boolean isEndParamBody = ';' == currentChar;
					if ( isEndParamBody ) {
						addNewParam(paramName, paramBody, params);
						
						paramName = new StringBuilder();
						paramBody = new StringBuilder();
						state = ParseStates.SearchParamName;
						continue;
					}
					paramBody.append(currentChar);
					break;
			}
		}
		
		String fieldNameText = fieldName.toString().trim();
		String fieldBodyText = fieldBody.toString().trim();
		
		boolean fieldBodyNotFound = fieldBodyText.length() == 0;
		if( fieldBodyNotFound ) {
			throw new MIMEParserException("field body not found: header=" + header);
		}

		boolean existsParam = paramName.length() > 0 && paramBody.length() > 0;
		if ( existsParam ) {
			addNewParam(paramName, paramBody, params);
		}
		
		boolean paramBodyNotFound = paramName.length() > 0 && paramBody.length() == 0;
		if( paramBodyNotFound ) {
			throw new MIMEParserException("param body not found: header=" + header);
		}
		
		MIMEHeader mimeHeader = new MIMEHeader(fieldNameText, fieldBodyText, params);
		
		return mimeHeader;
	}

	private void addNewParam(StringBuilder paramName, StringBuilder paramBody,
			List<MIMEParam> params) {
		String paramNameText = paramName.toString().trim();
		String paramBodyText = paramBody.toString().trim();
		boolean paramBodyQuoted = paramBodyText.startsWith("\"") && paramBodyText.endsWith("\"");
		if ( paramBodyQuoted ) {
			paramBodyText = paramBodyText.substring(1, paramBodyText.length() - 1);
		}
		
		MIMEParam param = new MIMEParam(paramNameText, paramBodyText);
		params.add(param);
	}

}

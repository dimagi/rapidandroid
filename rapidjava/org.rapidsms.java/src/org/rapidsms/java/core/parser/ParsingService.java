/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * Summary:
 */
package org.rapidsms.java.core.parser;

import java.util.Vector;

import org.rapidsms.java.core.model.Form;

/**
 * @author dmyung
 * @created Jan 21, 2009
 */
public class ParsingService {

	public enum ParserType { SIMPLEREGEX };
	
	private static SimpleRegexParser simpleRegex = new SimpleRegexParser();
	
//	public ParsingService() {
//		
//	}
	
	public static Vector<IParseResult> ParseMessage(Form form, String message) {
		switch(form.getParserType()) {
			case SIMPLEREGEX:
				return simpleRegex.ParseMessage(form, message);								
			default:
				throw new IllegalArgumentException("that parser does not exist");
		}		
	}
}

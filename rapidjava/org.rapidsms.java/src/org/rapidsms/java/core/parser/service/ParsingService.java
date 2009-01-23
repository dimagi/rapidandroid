/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * Summary:
 */
package org.rapidsms.java.core.parser.service;

import java.util.Vector;

import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.SimpleRegexParser;

/**
 * @author dmyung
 * @created Jan 21, 2009
 */
public class ParsingService {

	public enum ParserType { SIMPLEREGEX };
	
	private static SimpleRegexParser simpleRegexParser = new SimpleRegexParser();
	
//	public ParsingService() {
//		
//	}
	
	public static Vector<IParseResult> ParseMessage(Form form, String message) {
		switch(form.getParserType()) {
			case SIMPLEREGEX:
				return simpleRegexParser.ParseMessage(form, message);								
			default:
				throw new IllegalArgumentException("that parser does not exist");
		}		
	}
}

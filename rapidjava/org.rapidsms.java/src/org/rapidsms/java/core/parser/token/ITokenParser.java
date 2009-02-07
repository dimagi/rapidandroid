
package org.rapidsms.java.core.parser.token;

import org.rapidsms.java.core.parser.interpreter.IParseInterpreter;
import org.rapidsms.java.core.parser.IParseResult;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * 	
 * 		High level interface for parsing a field or token in a message
 * 	
 */
public interface ITokenParser {
	
	IParseResult Parse(String fragment);
	String getReadableName();	
	String getParsedDataType();	//this might be redundant
	IParseInterpreter getInterpreter();	
}

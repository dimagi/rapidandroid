
package org.rapidsms.java.core.parser.interpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 23, 2009
 * Summary:
 */
public class IntegerInterpreter implements IParseInterpreter {

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.interpreter.IParseInterpreter#interpretValue(java.lang.String)
	 */
	
	Pattern mPattern;
	
	public IntegerInterpreter () {
		//MUST HAVE ZERO GROUPS AND OTHER TOKENIZING JUNK
		mPattern = Pattern.compile("\\d+");
	}
	
	public Object interpretValue(String token) {
		Matcher m = mPattern.matcher(token);
		if(m.find()) {
			return Integer.valueOf(m.group(0));
		}
		return null;
	}

}

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 23, 2009
 * Summary:
 */
package org.rapidsms.java.core.parser.interpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dmyung
 * @created Jan 23, 2009
 */
public class FloatInterpreter implements IParseInterpreter {
	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.interpreter.IParseInterpreter#interpretValue(java.lang.String)
	 */
	
	Pattern mPattern;
	
	public FloatInterpreter () {
		//MUST HAVE ZERO GROUPS AND OTHER TOKENIZING JUNK
		mPattern = Pattern.compile("\\d+\\.*\\d*");
	}
	
	public Object interpretValue(String token) {
		Matcher m = mPattern.matcher(token);
		if(m.find()) {
			try {
				float f = Float.valueOf(m.group(0));
				return f;
			} catch (Exception ex) {
				System.out.println("Float Interpretation exception: " +token + " Message: "+ ex.getMessage());
				return null;
			}
			
			
		}
		return null;
	}
}

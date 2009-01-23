package org.rapidsms.java.core.parser.interpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 23, 2009
 * Summary:
 */
public class BooleanInterpreter implements IParseInterpreter {

	
	Pattern truePattern;
	Pattern falsePattern;
	
	
	public BooleanInterpreter() {
		truePattern = Pattern.compile("(t|true|y|yes|1)");
		falsePattern = Pattern.compile("(f|false|n|no|0)");
	}
	
	public Object interpretValue(String token) {
		Matcher trueMatch = truePattern.matcher(token);
		if(trueMatch.find()) {
			return true;
		} else {
			
			Matcher falseMatch = falsePattern.matcher(token);
			if(falseMatch.find()) {
				return false;
			}
		}
		return null;		
	}
}

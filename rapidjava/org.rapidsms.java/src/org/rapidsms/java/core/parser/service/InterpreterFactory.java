/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 23, 2009
 * Summary:
 */
package org.rapidsms.java.core.parser.service;

import org.rapidsms.java.core.parser.interpreter.BooleanInterpreter;
import org.rapidsms.java.core.parser.interpreter.FloatInterpreter;
import org.rapidsms.java.core.parser.interpreter.IParseInterpreter;
import org.rapidsms.java.core.parser.interpreter.IntegerInterpreter;
import org.rapidsms.java.core.parser.interpreter.StringInterpreter;

public class InterpreterFactory {
	public enum InterpreterType {
		BOOLEAN,
		NUMBER,
		RATIO,
		HEIGHT,
		LENGTH,
		WEIGHT,
		WORD		
	}
	
	
	private static IntegerInterpreter  integerInterpreter = new IntegerInterpreter();
	private static BooleanInterpreter  booleanInterpreter = new BooleanInterpreter();
	private static StringInterpreter  stringInterpreter = new StringInterpreter();
	private static FloatInterpreter  floatInterpreter = new FloatInterpreter();
	
	public static IParseInterpreter GetParseInterpreter(String typename) {
			if(typename.equals("boolean"))
				return booleanInterpreter;
			if(typename.equals("number"))
				return floatInterpreter;
			if(typename.equals("word"))			
				return stringInterpreter;
			if(typename.equals("float"))			
				return floatInterpreter;	
			if(typename.equals("integer"))			
				return integerInterpreter;
			
			throw new IllegalArgumentException("that parser does not exist " + typename);
		
	}
	
	
	public static IParseInterpreter GetParseInterpreter(InterpreterType type) {
		switch(type) {
			case BOOLEAN:
				return booleanInterpreter;
			case NUMBER:
				return floatInterpreter;
			case RATIO:
				return floatInterpreter;
			case HEIGHT:
				return integerInterpreter;
			case LENGTH:
				return integerInterpreter;
			case WEIGHT:
				return floatInterpreter;
			case WORD:
				return stringInterpreter;				
			default:
				throw new IllegalArgumentException("that parser does not exist " + type.ordinal());
		}		
	}

}

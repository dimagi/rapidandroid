/*
 *    rapidsdms-java - Java libraries for RapidSMS
 *    Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
		BOOLEAN, NUMBER, RATIO, HEIGHT, LENGTH, WEIGHT, WORD
	}

	private static IntegerInterpreter integerInterpreter = new IntegerInterpreter();
	private static BooleanInterpreter booleanInterpreter = new BooleanInterpreter();
	private static StringInterpreter stringInterpreter = new StringInterpreter();
	private static FloatInterpreter floatInterpreter = new FloatInterpreter();

	public static IParseInterpreter GetParseInterpreter(String typename) {
		if (typename.equals("boolean"))
			return booleanInterpreter;
		if (typename.equals("number"))
			return floatInterpreter;
		if (typename.equals("word"))
			return stringInterpreter;
		if (typename.equals("float"))
			return floatInterpreter;
		if (typename.equals("integer"))
			return integerInterpreter;

		throw new IllegalArgumentException("that parser does not exist " + typename);

	}

	public static IParseInterpreter GetParseInterpreter(InterpreterType type) {
		switch (type) {
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

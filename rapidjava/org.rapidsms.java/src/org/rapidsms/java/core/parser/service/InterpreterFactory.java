/*
 * Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
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

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

package org.rapidsms.java.core.parser.service;

import java.util.Vector;

import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.SimpleRegexParser;

/**
 * 
 * Static parsing service to marshall out different parsing methods.
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * 
 */
public class ParsingService {

	public enum ParserType {
		SIMPLEREGEX
	};

	private static SimpleRegexParser simpleRegexParser = new SimpleRegexParser();

	/**
	 * For a given message, call the appropriate parsing class and return the
	 * parse results.
	 * 
	 * @param form
	 * @param message
	 * @return
	 */
	public static Vector<IParseResult> ParseMessage(Form form, String message) {
		switch (form.getParserType()) {
			case SIMPLEREGEX:
				return simpleRegexParser.ParseMessage(form, message);
			default:
				throw new IllegalArgumentException("that parser does not exist");
		}
	}
}

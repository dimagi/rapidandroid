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

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
package org.rapidsms.java.core.parser.interpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 23, 2009 Summary:
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
		if (trueMatch.find()) {
			return true;
		} else {

			Matcher falseMatch = falsePattern.matcher(token);
			if (falseMatch.find()) {
				return false;
			}
		}
		return null;
	}
}

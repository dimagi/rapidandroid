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
 * 
 */
package org.rapidsms.java.core.parser;

import org.rapidsms.java.core.parser.token.ITokenParser;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 16, 2009
 * 
 *          A highly coupled hashmap for linking fields to a parsed result value
 *          from a given message.
 * 
 *          When parsing a form, the creator of this object, the IMessageParser
 *          will drop the results (whether successful or unsuccessful) of a
 *          parse per field into this object for return.
 * 
 * 
 */

public class SimpleParseResult implements IParseResult {
	ITokenParser fieldType;
	Object value;
	String token;

	public SimpleParseResult(ITokenParser fieldType, String token, Object val) {
		this.fieldType = fieldType;
		this.value = val;
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.IParseResult#getSource()
	 */

	public String getSource() {
		// TODO Auto-generated method stub
		return fieldType.getReadableName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.IParseResult#getValue()
	 */

	public Object getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.IParseResult#getParsedToken()
	 */
	public String getParsedToken() {
		// TODO Auto-generated method stub
		return token;
	}
}

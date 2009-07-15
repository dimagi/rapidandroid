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

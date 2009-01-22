/**
 * 
 */
package org.rapidsms.java.core.parser;

import java.util.HashMap;

import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;

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

public class SimpleParseResult  implements IParseResult {
	Field field;
	Object value;
	String token;
	
	public SimpleParseResult(Field field, String token, Object val) {
		this.field = field;
		this.value = val;
		this.token = token;
	}

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.IParseResult#getSource()
	 */
	
	public String getSource() {
		// TODO Auto-generated method stub
		return field.getName();
	}

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.IParseResult#getValue()
	 */
	
	public String getValue() {
		// TODO Auto-generated method stub
		return (String)value;
	}

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.IParseResult#getParsedToken()
	 */
	public String getParsedToken() {
		// TODO Auto-generated method stub
		return token;
	}
}

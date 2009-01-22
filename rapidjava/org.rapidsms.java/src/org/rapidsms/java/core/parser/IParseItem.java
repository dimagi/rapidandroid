
package org.rapidsms.java.core.parser;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * 	
 * 		High level interface for parsing a field or token in a message
 * 	
 */
public interface IParseItem {
	
	Object Parse(String fragment);
	String getItemName();	
	String getItemType();
}

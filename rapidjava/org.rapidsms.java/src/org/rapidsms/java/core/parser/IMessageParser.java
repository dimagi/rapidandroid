/**
 * 
 */
package org.rapidsms.java.core.parser;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 16, 2009
 * 
 * Interface for parsing method for forms.  
 * The application will rely upon these methods to determine the data from a message parse.
 * 
 */

public interface IMessageParser {
	boolean CanParse(String input);
	ParseResult ParseMessage(String input);
	String getName();
}

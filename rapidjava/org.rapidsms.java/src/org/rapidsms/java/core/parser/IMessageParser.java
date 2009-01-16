/**
 * 
 */
package org.rapidsms.java.core.parser;

/**
 * @author dmyung
 * @created Jan 16, 2009
 */

//
public interface IMessageParser {
	boolean CanParse(String input);
	ParseResult ParseMessage(String input);
	String getName();
}

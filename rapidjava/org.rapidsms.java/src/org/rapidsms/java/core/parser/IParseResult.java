/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * Summary:
 */
package org.rapidsms.java.core.parser;

/**
 * @author dmyung
 * @created Jan 21, 2009
 */
public interface IParseResult {
	String getSource();

	String getParsedToken();

	Object getValue();
}

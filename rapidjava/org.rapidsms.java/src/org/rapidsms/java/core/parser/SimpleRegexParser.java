/**
 * 
 */
package org.rapidsms.java.core.parser;

/**
 * @author dmyung
 * @created Jan 16, 2009
 */
public class SimpleRegexParser implements IMessageParser {

	public SimpleRegexParser() {
		
	}

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.IMessageParser#CanParse(java.lang.String)
	 */
	@Override
	public boolean CanParse(String input) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.IMessageParser#ParseMessage(java.lang.String)
	 */
	@Override
	public ParseResult ParseMessage(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.IMessageParser#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "simpleregex";
	}
	
	
	

}

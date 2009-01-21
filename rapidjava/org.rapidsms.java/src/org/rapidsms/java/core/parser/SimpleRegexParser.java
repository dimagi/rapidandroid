/**
 * 
 */
package org.rapidsms.java.core.parser;

/**
 * @author dmyung
 * @created Jan 16, 2009
 * 
 *          The first instance of a message parser for RapidAndroid
 * 
 *          The objective for this parser is to have a simple, greedy order
 *          dependent parse of a message
 * 
 *          for a given message MSG and a form F with fields [a,b,c,d,e]
 * 
 *          where the fields have regexes for each "token" they want to parse
 *          out (height measurement or a string for example)
 * 
 *          This parser will iterate through each field in order, greedily try
 *          to find the *first* instance of the match it can find from its regex
 *          Slice out the substring of the first match from the original message
 *          MSG, and continue onto the next field again.
 */
public class SimpleRegexParser implements IMessageParser {

	public SimpleRegexParser() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rapidsms.java.core.parser.IMessageParser#CanParse(java.lang.String)
	 */
	@Override
	public boolean CanParse(String input) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rapidsms.java.core.parser.IMessageParser#ParseMessage(java.lang.String
	 * )
	 */
	@Override
	public ParseResult ParseMessage(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.IMessageParser#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "simpleregex";
	}

}

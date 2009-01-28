/**
 * 
 */
package org.rapidsms.java.core.parser;

import java.util.Vector;

import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.token.ITokenParser;

/**
 * @author Daniel Myung dmyung@dimagi.com
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
	
	public Vector<IParseResult> ParseMessage(Form f, String input) {
		System.out.println("");
		System.out.println("");
		System.out.println("********** begin ParseMessage ************");
		
		//ok, for this iteration, we're going to greedily determine if this is a message we can fracking parse.
		
		String prefix = f.getPrefix();
		System.out.println("what's the fracking form prefix: " + prefix);
		input = input.toLowerCase().trim();
		if(input.startsWith(prefix + " ")) {
			
			input = input.substring(prefix.length()).trim();
		} else {
			return null;
		}
		
		Vector<IParseResult> results = new Vector<IParseResult>();		
		Field[] fields = f.getFields();
		int length = fields.length;
		
		for(int i = 0; i < length; i++) {
			ITokenParser parser = fields[i].getFieldType();
			System.out.println("Begin field parse: [" + fields[i].getName() + "] on input: {" + input + "}");
			IParseResult res = parser.Parse(input);
			
			//ok, so we got the res, so we need to subtract the parsed string if at all possible.
			if(res != null) {
				String justParsedToken = res.getParsedToken();
				int tokLen = justParsedToken.length();
				System.out.println("Parsed input:" + input);
				System.out.println("Just parsed:" + justParsedToken + "##");
				int tokStart = input.indexOf(justParsedToken);				
				System.out.println("tokLen: " + tokLen);
				
				if(tokStart > 0) {
					tokStart = tokStart -1; //need to shift over one for the shiftage
				}
				//int tokRest = tokStart+1;
				
				System.out.println("tokStart: " + tokStart);
				System.out.println("inputLen: " + input.length());
				String newInput = input.substring(0, tokStart) + input.substring(tokLen);
				
				input = newInput.trim();
			}
			results.add(res);
		}		
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.IMessageParser#getName()
	 */
	
	public String getName() {
		// TODO Auto-generated method stub
		return "simpleregex";
	}	

}

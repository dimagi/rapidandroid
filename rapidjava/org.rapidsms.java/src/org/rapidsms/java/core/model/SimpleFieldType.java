/**
 * 
 */
package org.rapidsms.java.core.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rapidsms.java.core.parser.IParseItem;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.SimpleParseResult;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 16, 2009
 * 
 *          Simple single regex based field type parser.
 * 
 */

public class SimpleFieldType implements IParseItem {
	private String datatype;
	private int id;
	private String regex;
	private String name;
	
	private Pattern mPattern;

	public SimpleFieldType(int id, String datatype, String regex, String name) {
		this.id = id;
		this.datatype = datatype;
		this.regex = regex;
		this.name = name;
		mPattern = Pattern.compile(this.regex);
		
	}

	public SimpleFieldType() {

	}

	/**
	 * @return the name
	 */
	public String getDataType() {
		return datatype;
	}

	

	/**
	 * @param name
	 *            the name to set
	 */
	public void setDataType(String datatype) {
		this.datatype = datatype;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex
	 *            the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.IParseItem#getType()
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return datatype;
	}
	
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.rapidsms.java.core.parser.IParseItem#Parse(java.lang.String)
	 */
	@Override
	public Object Parse(String fragment) {
		Matcher matcher = mPattern.matcher(fragment);				
		if(matcher.matches()) {
			
			return matcher.group(0);
		} else {
			return null;
		}
		
	}

}

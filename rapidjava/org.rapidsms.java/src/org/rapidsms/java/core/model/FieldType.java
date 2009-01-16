/**
 * 
 */
package org.rapidsms.java.core.model;

/**
 * @author dmyung
 * @created Jan 16, 2009
 */

//this is tightly coupled to the SimpleRegexParser
public class FieldType {
	private String datatype;
	private int id;
	private String regex;
	private String name;
	
	public FieldType(int id, String datatype, String regex, String name) {
		this.id = id;
		this.datatype = datatype;
		this.regex = regex;
		this.name = name;
	}
	
	public FieldType() {
		
	}

	/**
	 * @return the name
	 */
	public String getDataType() {
		return datatype;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
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
	 * @param id the id to set
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
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	

}

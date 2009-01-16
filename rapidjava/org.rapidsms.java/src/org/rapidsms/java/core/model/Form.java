/**
 * 
 */
package org.rapidsms.java.core.model;


import org.rapidsms.java.core.parser.IMessageParser;
import org.rapidsms.java.core.parser.SimpleRegexParser;

/**
 * @author dmyung
 * @created Jan 16, 2009
 */
public class Form {

	private int formId;
	private String formName;
	private String prefix;
	private String description;
	private IMessageParser parser;
	private Field[] fields;
	
	public Form() {

	}
	
	public Form(int id, String name, String prefix, String desc, String parsetype, Field[] fields) {
		this.formId = id;
		formName = name;
		this.prefix = prefix;
		this.description = desc;
		this.fields = fields;
		if(parsetype.equals("simpleregex")) {
			this.parser = new SimpleRegexParser();
		}
	}
	
	/**
	 * @return the fields
	 */
	public Field[] getFields() {
		return fields;
	}
	/**
	 * @return the formId
	 */
	public int getFormId() {
		return formId;
	}
	/**
	 * @param formId the formId to set
	 */
	
	/**
	 * @return the formName
	 */
	public String getFormName() {
		return formName;
	}
	/**
	 * @param formName the formName to set
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}
	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the parser
	 */
	public IMessageParser getParser() {
		return parser;
	}
	/**
	 * @param parser the parser to set
	 */
	public void setParser(IMessageParser parser) {
		this.parser = parser;
	}
	
}

/*
 *    rapidsdms-java - Java libraries for RapidSMS
 *    Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


package org.rapidsms.java.core.model;

import org.rapidsms.java.core.parser.token.ITokenParser;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 16, 2009
 * 
 *          Field model for the initial parsing flow for RapidAndroid
 * 
 */
public class Field {
	private int fieldId;
	private int sequenceId;
	private String name;
	private String description;
	private ITokenParser fieldType;

	public Field(int id, int sequence, String name, String description, ITokenParser ftype) {
		this.fieldId = id;
		this.sequenceId = sequence;
		this.name = name;
		this.description = description;
		this.fieldType = ftype;
	}

	public Field() {
		fieldId = -1;
		description = "";
		sequenceId = -1;
		fieldType = null;
	}

	/**
	 * @return the fieldId
	 */
	public int getFieldId() {
		return fieldId;
	}

	/**
	 * @param fieldId
	 *            the fieldId to set
	 */
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}

	/**
	 * @return the sequenceId
	 */
	public int getSequenceId() {
		return sequenceId;
	}

	/**
	 * @param sequenceId
	 *            the sequenceId to set
	 */
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the fieldType
	 */
	public ITokenParser getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType
	 *            the fieldType to set
	 */
	public void setFieldType(ITokenParser fieldType) {
		this.fieldType = fieldType;
	}

}

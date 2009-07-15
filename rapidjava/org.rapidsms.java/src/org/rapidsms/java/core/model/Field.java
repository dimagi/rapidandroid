/*
 * Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
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

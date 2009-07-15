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

/**
 * 
 */
package org.rapidsms.java.core.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.SimpleParseResult;
import org.rapidsms.java.core.parser.interpreter.IParseInterpreter;
import org.rapidsms.java.core.parser.service.InterpreterFactory;
import org.rapidsms.java.core.parser.token.ITokenParser;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 16, 2009
 * 
 *          Simple single regex based field type parser.
 * 
 */

public class SimpleFieldType implements ITokenParser {
	private String datatype;
	private int id;
	private String regex;
	private String mReadableName;
	private Pattern mPattern;
	private IParseInterpreter interpreter;

	public SimpleFieldType(int id, String datatype, String regex, String name) {
		this.id = id;
		this.datatype = datatype;
		this.regex = regex;
		this.mReadableName = name;

		mPattern = Pattern.compile(this.regex);

		interpreter = InterpreterFactory.GetParseInterpreter(datatype);

	}

	public SimpleFieldType() {

	}

	/**
	 * @return the mReadableName
	 */
	public String getDataType() {
		return datatype;
	}

	/**
	 * @param mReadableName
	 *            the mReadableName to set
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.ITokenParser#getType()
	 */

	public String getParsedDataType() {
		// TODO Auto-generated method stub
		return datatype;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.ITokenParser#Parse(java.lang.String)
	 */

	public IParseResult Parse(String fragment) {

		// Pattern mPattern;
		// mPattern = Pattern.compile(regex);
		Matcher matcher = mPattern.matcher(fragment);
		boolean isMatched = matcher.find();
		int maxSize = -1;
		int maxGroup = -1;
		int minstart = 0;
		int maxend = 0;

		if (isMatched) {
			for (int q = 0; q < matcher.groupCount(); q++) {

				if (matcher.group(q) == null) {
					// System.out.println("why the frack is this null " + q +
					// " count: " + matcher.groupCount());
					continue;
				}
				int currsize = matcher.group(q).length();

				if (currsize > maxSize) {
					maxGroup = q;
					maxSize = currsize;
				}
			}
			minstart = matcher.start(maxGroup);
			maxend = matcher.end(maxGroup);
			// System.out.println(matcher.group(maxGroup));
		} else {
			return null;
		}

		if (minstart < maxend) {
			// System.out.println("\t\tFragmenting: " + minstart + "-" +
			// maxend);
			String parsed = fragment.substring(minstart, maxend);

			parsed = parsed.trim();
			// System.out.println("\t\tMatched fragment: ##" + parsed + "##");
			SimpleParseResult res = new SimpleParseResult(this, parsed, getInterpreter().interpretValue(parsed));

			return res;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.token.ITokenParser#getInterpreter()
	 */
	public IParseInterpreter getInterpreter() {
		// TODO Auto-generated method stub
		return interpreter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidsms.java.core.parser.token.ITokenParser#getName()
	 */
	public String getReadableName() {
		// TODO Auto-generated method stub
		return mReadableName;
	}

}

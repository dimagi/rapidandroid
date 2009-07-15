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

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 26, 2009 Summary:
 */
public class Monitor {

	public static final int COL_ID = 0;
	public static final int COL_FIRSTNAME = 1;
	public static final int COL_LASTNAME = 2;
	public static final int COL_ALIAS = 3;
	public static final int COL_PHONE = 4;
	public static final int COL_EMAIL = 5;
	public static final int COL_MESSAGECOUNT = 6;
	public static final int COL_RECEIVE_REPLY = 7;

	/**
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param alias
	 * @param phone
	 * @param email
	 * @param incomingMessages
	 */
	public Monitor(int id, String firstName, String lastName, String alias, String phone, String email,
			int incomingMessages, boolean receiveReply) {
		super();
		mId = id;
		mFirstName = firstName;
		mLastName = lastName;
		mAlias = alias;
		mPhone = phone;
		mEmail = email;
		mIncomingMessages = incomingMessages;
		mReceiveReply = receiveReply;

	}

	private int mId;
	private String mFirstName;
	private String mLastName;
	private String mAlias;
	private String mPhone;
	private String mEmail;
	private int mIncomingMessages;
	private boolean mReceiveReply = false;

	/**
	 * @return the mId
	 */
	public int getID() {
		return mId;
	}

	/**
	 * @return the mFirstName
	 */
	public String getFirstName() {
		return mFirstName;
	}

	/**
	 * @return the mLastName
	 */
	public String getLastName() {
		return mLastName;
	}

	/**
	 * @return the mAlias
	 */
	public String getAlias() {
		return mAlias;
	}

	/**
	 * @return the mPhone
	 */
	public String getPhone() {
		return mPhone;
	}

	/**
	 * @return the mEmail
	 */
	public String getEmail() {
		return mEmail;
	}

	/**
	 * @return the mIncomingMessages
	 */
	public int getIncomingMessages() {
		return mIncomingMessages;
	}

	/**
	 * @return the reply preference
	 */
	public boolean getReplyPreference() {
		return mReceiveReply;
	}

}

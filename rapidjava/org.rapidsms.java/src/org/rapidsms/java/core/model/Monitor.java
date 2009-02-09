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

package org.rapidsms.java.core.model;

import java.util.Date;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 26, 2009
 * Summary:
 */
public class Message {
	
	public static final int COL_ID = 0;	
	public static final int COL_PHONE = 1;	//deprecated
	public static final int COL_MONITOR = 2;
	public static final int COL_TIME = 3;
	public static final int COL_MESSAGE = 4;
	public static final int COL_IS_OUTGOING = 5;
	public static final int COL_IS_VIRTUAL = 6;	
	
	public static final int COL_PARSED_ID = 0;
	public static final int COL_PARSED_MESSAGE_ID = 1;
	public static final int COL_PARSED_FIELDS_OFFSET = 2;	
	
	public Message(int id, String message, Date timestamp, Monitor monitor) {
		this.mId = id;
		this.mMessageText = message;
		this.mMonitor = monitor;
		this.mTimestamp = timestamp;
	}
	
	/**
	 * @return the mMessageText
	 */
	public String getMessageText() {
		return mMessageText;
	}
	/**
	 * @return the mTimestamp
	 */
	public Date getTimestamp() {
		return mTimestamp;
	}
	/**
	 * @return the mMonitorPhone
	 */
	public Monitor getMonitor() {
		return mMonitor;
	}
	
	/**
	 * @return the id
	 */
	public int getID() {
		return mId;
	}

	private int mId;
	private String mMessageText;
	private Date mTimestamp;
	private Monitor mMonitor;
	

}

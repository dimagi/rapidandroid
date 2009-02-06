package org.rapidandroid.content.translation;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.model.Monitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Helper class to simplify the insertion and querying of raw SMS messages from
 * the content provider.  
 * 
 * <br>Goal for this class is to return Message objects instead of cursors. 
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * 
 * 
 */

public class MessageTranslator {

	private static HashMap<Integer, Monitor> mMonitorHash = new HashMap<Integer, Monitor>();
	private static HashMap<String, Monitor> mMonitorHashByPhone = new HashMap<String, Monitor>();
	
	/**
	 * To ease DB hits on the insert of messages, the monitorhash has two hastables indexed by phone number and ID.
	 * We need to update this thing on each new insert.
	 * @param context
	 */
	public static synchronized void updateMonitorHash(Context context) {

		mMonitorHash = new HashMap<Integer, Monitor>();
		mMonitorHashByPhone = new HashMap<String, Monitor>();
		Cursor monitorCursor = context.getContentResolver().query(RapidSmsDBConstants.Monitor.CONTENT_URI, null, null,
																	null, null);
		if (monitorCursor.getCount() == 0) {
			monitorCursor.close();
			return;
		}
		

		monitorCursor.moveToFirst();

		do {
			// (int id, String firstName, String lastName, String alias,
			// String phone, String email, int incomingMessages) {
			
		
			
			Monitor newMonitor = new Monitor(monitorCursor.getInt(Monitor.COL_ID),
												monitorCursor.getString(Monitor.COL_FIRSTNAME),
												monitorCursor.getString(Monitor.COL_LASTNAME),
												monitorCursor.getString(Monitor.COL_ALIAS),
												monitorCursor.getString(Monitor.COL_PHONE),
												monitorCursor.getString(Monitor.COL_EMAIL),
												monitorCursor.getInt(Monitor.COL_MESSAGECOUNT),
												monitorCursor.getInt(Monitor.COL_RECEIVE_REPLY)==1);
			mMonitorHash.put(Integer.valueOf(newMonitor.getID()), newMonitor);
			mMonitorHashByPhone.put(newMonitor.getPhone(), newMonitor);
		} while (monitorCursor.moveToNext());
		monitorCursor.close();
	}

	/**
	 * Get a handle to monitor for a given ID
	 * @param context
	 * @param monitorID
	 * @return
	 */
	public static synchronized Monitor GetMonitor(Context context, int monitorID) {
		Integer monID = Integer.valueOf(monitorID);
		if (mMonitorHash.containsKey(monID)) {
			return mMonitorHash.get(monID);
		} else {
			throw new IllegalArgumentException(
												"Error in application state.  The monitor hash should always be up to date when querying");
		}
	}

	/**
	 * Get a monitor or insert a new one based upon a given phone number
	 *  
	 * @returns The monitor found or created.  This is necesary to do link a new message to a Monitor.ID
	 * 
	 */
	public static synchronized Monitor GetMonitorAndInsertIfNew(Context context, String phone) {
		
		
		if (mMonitorHashByPhone.containsKey(phone)) {
			return mMonitorHashByPhone.get(phone);
		} else {
			ContentValues cv = new ContentValues();
			cv.put(RapidSmsDBConstants.Monitor.PHONE, phone);
			Uri newUri = context.getContentResolver().insert(RapidSmsDBConstants.Monitor.CONTENT_URI, cv);
			updateMonitorHash(context);
			return mMonitorHashByPhone.get(phone);
		}
	}

	public static synchronized Message GetMessage(Context context, int messageID) {
		if (mMonitorHash == null) {
			updateMonitorHash(context);
		}

		Uri getMessageUri = Uri.parse(RapidSmsDBConstants.Message.CONTENT_URI_STRING + messageID);

		Cursor msgCursor = context.getContentResolver().query(getMessageUri, null, null, null, null);
		msgCursor.moveToFirst();
		if (msgCursor.getCount() != 1) {
			return null;
		} else {
			try {
				String datestring = msgCursor.getString(Message.COL_TIME);
				Date msgDate = Message.SQLDateFormatter.parse(datestring);
				
				String recvstring = msgCursor.getString(Message.COL_RECEIVE_TIME);
				Date recvDate = msgDate;	//for old entries, should we set it to null or just copy it?
				if(recvstring == null || recvstring == "") {
					recvDate = Message.SQLDateFormatter.parse(datestring);
				}
				

				Message newMessage = new Message(
													msgCursor.getInt(Message.COL_ID),
													msgCursor.getString(Message.COL_MESSAGE),
													msgDate,
													mMonitorHash.get(Integer.valueOf(msgCursor.getInt(Message.COL_MONITOR))),
													recvDate
				);
				msgCursor.close();
				return newMessage;
			} catch (Exception ex) {
				throw new IllegalArgumentException("Invalid state");
			}
		}
	}

	public static synchronized Message[] GetMessages(Context context, int[] messages) {
		if (mMonitorHash == null) {
			updateMonitorHash(context);
		}
		Uri getMessageUri = RapidSmsDBConstants.Message.CONTENT_URI;
		String whereclause = "_id in (";
		int length = messages.length;
		for (int i = 0; i < length; i++) {
			whereclause += messages[i];
			if (i < length - 1) {
				whereclause += ",";
			}
		}
		whereclause += ")";

		Cursor msgCursor = context.getContentResolver().query(getMessageUri, null, whereclause, null, "time DESC");
		int retlen = msgCursor.getCount();
		Message[] ret = new Message[retlen];

		msgCursor.moveToFirst();
		for (int i = 0; i < retlen; i++) {
			try {
				String datestring = msgCursor.getString(Message.COL_TIME);
				Date msgDate = Message.SQLDateFormatter.parse(datestring);
				
				String recvstring = msgCursor.getString(Message.COL_RECEIVE_TIME);
				Date recvDate = msgDate;	//for old entries, should we set it to null or just copy it?

				if(recvstring == null || recvstring == "") {
					recvDate = Message.SQLDateFormatter.parse(datestring);
				}
				
				Message newMessage = new Message(
													msgCursor.getInt(Message.COL_ID),
													msgCursor.getString(Message.COL_MESSAGE),
													msgDate,
													mMonitorHash.get(Integer.valueOf(msgCursor.getInt(Message.COL_MONITOR))),
													recvDate
													);
				ret[i] = newMessage;
			} catch (Exception ex) {
				// unable to parse datetime format

			}
			msgCursor.moveToNext();
		}
		msgCursor.close();
		return ret;
	}

}

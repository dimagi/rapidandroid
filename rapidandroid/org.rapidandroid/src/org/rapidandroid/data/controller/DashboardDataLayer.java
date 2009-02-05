package org.rapidandroid.data.controller;

import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Feb 4, 2009
 * Summary:
 */
public class DashboardDataLayer {

	private static SmsDbHelper mDbHelper;
	private static SQLiteDatabase mDb;
	
	public synchronized static Cursor getCursorForFormData(Context context, Form f, int count) {
		if(mDb != null) {
			mDb.close();
			mDb = null;
		}
		mDbHelper = new SmsDbHelper(context);
		mDb = mDbHelper.getReadableDatabase();
		StringBuilder query = new StringBuilder();
		query.append("select " + RapidSmsDBConstants.FormData.TABLE_PREFIX);
		query.append(f.getPrefix() + ".*, rapidandroid_message.message, rapidandroid_message.time, rapidandroid_monitor.phone ");
		query.append(" from " + RapidSmsDBConstants.FormData.TABLE_PREFIX + f.getPrefix());
		query.append(" join rapidandroid_message on (");
		query.append(RapidSmsDBConstants.FormData.TABLE_PREFIX + f.getPrefix());
		query.append(".message_id = rapidandroid_message._id");
		query.append(") ");
		query.append(" join rapidandroid_monitor on (rapidandroid_message.monitor_id = rapidandroid_monitor._id) ");
		query.append(" ORDER BY rapidandroid_message.time DESC LIMIT ").append(count);
		
		Cursor cr = mDb.rawQuery(query.toString(), null);

		mDbHelper.close();
		return cr;
	}
	
	public synchronized static Cursor getCursorForRawMessages(Context context,int count) {		
		if(mDb != null) {
			mDb.close();
			mDb = null;
		}
		mDbHelper = new SmsDbHelper(context);
		mDb = mDbHelper.getReadableDatabase();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from rapidandroid_message ORDER BY time DESC LIMIT " ).append(count);
		Cursor cr = mDb.rawQuery(sb.toString(), null);
		mDbHelper.close();
		return cr;
	}
	
	
	
	
}

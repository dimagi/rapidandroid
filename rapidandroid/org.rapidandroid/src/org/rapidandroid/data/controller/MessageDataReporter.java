package org.rapidandroid.data.controller;

import java.text.ParseException;
import java.util.Date;

import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.model.Message;

import android.content.Context;
import android.database.Cursor;

public class MessageDataReporter {

	public static Date getOldestMessageDate(Context context) {
		StringBuilder query = new StringBuilder();
		query.append("select min(time) ");		
		query.append(" from rapidandroid_message");
		SmsDbHelper mHelper = new SmsDbHelper(context);
		
		Cursor cr = mHelper.getReadableDatabase().rawQuery(query.toString(), null);
		cr.moveToFirst();
		String dateString = cr.getString(0);
		Date ret = new Date();
		try {
			ret = Message.SQLDateFormatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		cr.close();
		return ret;
	}
	
}

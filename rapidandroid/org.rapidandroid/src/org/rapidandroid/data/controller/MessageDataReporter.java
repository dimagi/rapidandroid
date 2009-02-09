/*
 *    rapidandroid - SMS gateway for the android platform
 *    Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.rapidandroid.data.controller;

import java.text.ParseException;
import java.util.Date;

import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.model.Message;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MessageDataReporter {

	public static Date getOldestMessageDate(Context context) {
		StringBuilder query = new StringBuilder();
		query.append("select min(time) ");
		query.append(" from rapidandroid_message");
		SmsDbHelper mHelper = new SmsDbHelper(context);
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cr = db.rawQuery(query.toString(), null);
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
		db.close();
		mHelper.close();
		return ret;
	}

}

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

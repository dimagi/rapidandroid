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
package org.rapidandroid.view.adapter;

import java.util.Date;

import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.model.Monitor;

import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MessageCursorAdapter extends CursorAdapter {

	public MessageCursorAdapter(Context context, Cursor c) {
		super(context, c, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#bindView(android.view.View,
	 * android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (view != null) {
			int MonitorID = cursor.getInt(2);
			String timestamp = cursor.getString(3);
			String message = cursor.getString(4);
			boolean isoutgoing = Boolean.parseBoolean(cursor.getString(4));
			Date hackDate = new Date();
			boolean success = false;
			try {
				hackDate = Message.SQLDateFormatter.parse(timestamp);
				success = true;
			} catch (Exception ex) {
				success = false;
			}

			SimpleMessageView srv = (SimpleMessageView) view;
			srv.setData(message, hackDate, MonitorID, isoutgoing);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#newView(android.content.Context,
	 * android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		int MonitorID = cursor.getInt(2);
		String timestamp = cursor.getString(3);
		String message = cursor.getString(4);
		boolean isoutgoing = Boolean.parseBoolean(cursor.getString(4));
		Date hackDate = new Date();

		SimpleMessageView srv = new SimpleMessageView(context, message, hackDate, MonitorID, isoutgoing);
		return srv;
	}

	private class SimpleMessageView extends TableLayout {

		private TableRow mHeaderRow;
		private TextView txvDate;
		private TextView txvFrom;
		private TextView txvMessage;

		public SimpleMessageView(Context context, String message, Date timestamp, int monitorID, boolean isOutgoing) {
			super(context);
			mHeaderRow = new TableRow(context);

			txvDate = new TextView(context);
			txvDate.setTextSize(16);
			txvDate.setPadding(3, 3, 3, 3);
			txvDate.setGravity(Gravity.LEFT);
			txvFrom = new TextView(context);
			txvFrom.setTextSize(16);
			txvFrom.setPadding(3, 3, 8, 3);
			txvFrom.setGravity(Gravity.RIGHT);

			// this.addView(txvHeader, new
			// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
			// LayoutParams.WRAP_CONTENT));
			mHeaderRow.addView(txvDate);
			mHeaderRow.addView(txvFrom);
			addView(mHeaderRow);

			txvMessage = new TextView(context);
			txvMessage.setTextSize(12);
			txvMessage.setPadding(8, 2, 8, 2);
			// this.addView(txvMessage, new
			// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
			// LayoutParams.WRAP_CONTENT));
			this.addView(txvMessage);

			this.setColumnStretchable(0, true);
			this.setColumnStretchable(1, true);

			setData(message, timestamp, monitorID, isOutgoing);
			// TODO Auto-generated constructor stub
		}

		public void setData(String message, Date timestamp, int monitorID, boolean isOutgoing) {
			txvDate.setText(Message.DisplayDateTimeFormat.format(timestamp));

			Monitor m = MessageTranslator.GetMonitor(getContext(), monitorID);
			txvFrom.setText(m.getPhone());

			txvMessage.setText(message);
		}

	}

}

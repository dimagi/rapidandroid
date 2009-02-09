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

import org.rapidandroid.view.SingleGridRowView;
import org.rapidsms.java.core.model.Form;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CursorAdapter;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 27, 2009 Summary:
 */
public class FormDataGridCursorAdapter extends CursorAdapter {

	Form mForm;
	Context mContext;
	int mWidth;
	int mColWidth;

	/**
	 * @param context
	 * @param c
	 */
	public FormDataGridCursorAdapter(Context context, Form form, Cursor c, int screenWidth) {
		super(context, c);
		mForm = form;
		mContext = context;
		mWidth = screenWidth;
		mColWidth = screenWidth / (c.getColumnCount() - 5);

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
			SingleGridRowView srv = (SingleGridRowView) view;
			srv.setData(cursor);
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
		SingleGridRowView srv = new SingleGridRowView(context, cursor, mColWidth);
		return srv;
	}

}

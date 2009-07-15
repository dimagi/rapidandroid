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

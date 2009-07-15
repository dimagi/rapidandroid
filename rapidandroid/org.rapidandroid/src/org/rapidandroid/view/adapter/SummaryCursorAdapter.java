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

import java.util.HashMap;

import org.rapidandroid.view.SummaryCursorView;
import org.rapidsms.java.core.model.Form;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009 Summary:
 */
public class SummaryCursorAdapter extends CursorAdapter {

	// private int newCount = 0;
	// private int bindCount = 0;
	private int mLoadViewCount = 0;

	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
		mLoadViewCount = 0;
	}

	@Override
	public void changeCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		super.changeCursor(cursor);
		mLoadViewCount = 0;
	}

	@Override
	protected void init(Context context, Cursor c, boolean autoRequery) {
		// TODO Auto-generated method stub
		super.init(context, c, autoRequery);
		mLoadViewCount = 0;
	}

	private Form mForm;
	String[] mFields;

	private static Boolean bFalse = Boolean.valueOf(false);
	private static Boolean bTrue = Boolean.valueOf(true);

	private HashMap<Integer, Boolean> mExpanded;

	/**
	 * @param context
	 * @param c
	 */
	public SummaryCursorAdapter(Context context, Cursor c, Form f) {
		super(context, c, false);
		mForm = f;
		mFields = new String[mForm.getFields().length];
		for (int i = 0; i < mFields.length; i++) {
			mFields[i] = mForm.getFields()[i].getName();
		}
		mExpanded = new HashMap<Integer, Boolean>();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#bindView(android.view.View,
	 * android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		SummaryCursorView pmcv = (SummaryCursorView) view;
		pmcv.setData(cursor);
		Integer intpos = Integer.valueOf(cursor.getPosition());
		if (!mExpanded.containsKey(intpos)) {
			mExpanded.put(intpos, bFalse);
		}
		pmcv.setExpanded(mExpanded.get(intpos).booleanValue());

		// bindCount++;
		mLoadViewCount++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#newView(android.content.Context,
	 * android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Integer intpos = Integer.valueOf(cursor.getPosition());
		mExpanded.put(intpos, bFalse);
		// newCount++;
		return new SummaryCursorView(context, cursor, mFields, false);

	}

	public void toggle(int position) {
		Integer intpos = Integer.valueOf(position);
		if (!mExpanded.containsKey(intpos)) {
			mExpanded.put(intpos, bTrue);
		}
		mExpanded.put(intpos, Boolean.valueOf(!mExpanded.get(intpos).booleanValue()));
		notifyDataSetChanged();
	}

}

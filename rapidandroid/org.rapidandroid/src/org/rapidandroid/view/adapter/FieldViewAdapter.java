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

import org.rapidandroid.view.SimpleFieldView;
import org.rapidsms.java.core.model.Field;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Feb 5, 2009 Summary:
 */
public class FieldViewAdapter extends BaseAdapter {

	private Context mContext;

	public FieldViewAdapter(Context context, Field[] fields) {
		mContext = context;
		mFields = fields;
	}

	private Field[] mFields;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		// TODO Auto-generated method stub
		return mFields.length;
	}

	public Object getItem(int position) {
		return position;
	}

	/**
	 * Use the array index as a unique id.
	 * 
	 * @see android.widget.ListAdapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		SimpleFieldView fv;
		Field f = mFields[position];

		if (convertView == null) {
			fv = new SimpleFieldView(mContext, f);
		} else {
			fv = (SimpleFieldView) convertView;
			fv.setField(f);
		}
		return fv;
	}

}

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

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
package org.rapidandroid.view;

import org.rapidsms.java.core.model.Field;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Feb 5, 2009 Summary:
 */
public class SimpleFieldView extends TableLayout {

	private Field mField;
	private TableRow mTopRow;
	private TextView mFieldName;
	private TextView mFieldType;
	private TextView mFieldDesc;

	/**
	 * @param context
	 */
	public SimpleFieldView(Context context, Field field) {
		super(context);
		mField = field;
		// TODO Auto-generated constructor stub
		mTopRow = new TableRow(context);
		mFieldName = new TextView(context);
		mFieldName.setTextSize(18);
		mFieldName.setPadding(4, 4, 4, 4);
		mFieldName.setGravity(Gravity.LEFT);

		mFieldType = new TextView(context);
		mFieldType.setTextSize(16);
		mFieldType.setPadding(4, 4, 4, 8);
		mFieldType.setGravity(Gravity.RIGHT);
		mTopRow.addView(mFieldName);
		mTopRow.addView(mFieldType);

		mFieldDesc = new TextView(context);
		mFieldDesc.setPadding(12, 4, 4, 12);
		mFieldDesc.setTextSize(14);
		this.setColumnStretchable(0, true);
		this.setColumnStretchable(1, true);

		this.addView(mTopRow);
		this.addView(mFieldDesc);
		setField(mField);
	}

	public void setField(Field field) {
		mFieldName.setText(field.getName());
		mFieldType.setText("[" + field.getFieldType().getReadableName() + "]");
		mFieldDesc.setText(field.getDescription());
	}

}

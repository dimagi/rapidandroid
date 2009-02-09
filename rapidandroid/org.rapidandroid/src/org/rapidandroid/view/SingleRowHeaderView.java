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

import java.util.Vector;

import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 27, 2009
 * 
 */
public class SingleRowHeaderView extends TableRow {
	private int mColCount;

	TableRow mRow;
	TextView mMessageIDCol;
	TextView mMonitorCol;
	Vector<TextView> mDataCols;

	/**
	 * @param context
	 */
	public SingleRowHeaderView(Context context, Form f, int screenWidth) {
		super(context);
		int itemCounter = 0;

		// mMessageIDCol = new TextView(context);
		// mMessageIDCol.setText("ID |");
		// mMessageIDCol.setGravity(1);
		// mRow.addView(mMessageIDCol, itemCounter++);
		//		
		// mMonitorCol = new TextView(context);
		// mMonitorCol.setText(" Mon | ");
		// mMonitorCol.setGravity(1);
		// mRow.addView(mMonitorCol, itemCounter++);

		mDataCols = new Vector<TextView>();
		mColCount = f.getFields().length;
		int width = screenWidth / mColCount;
		Field[] fields = f.getFields();
		String suffix = " | ";
		for (int i = 0; i < mColCount; i++) {
			TextView coldata = new TextView(getContext());
			coldata.setText(fields[i].getName());
			coldata.setTextSize(14);
			// coldata.setTextAppearance(context, android.R.style)
			coldata.setGravity(Gravity.LEFT);
			coldata.setEllipsize(TruncateAt.END); // makeit ellipsize instead of
													// spillage!!!
			coldata.setWidth(width);

			mDataCols.add(coldata);
			addView(coldata);
		}
		this.setPadding(2, 2, 8, 2);
		// this.setBackgroundResource(android.R.drawable.);
	}

	public int getColCount() {
		return mColCount;
	}
}

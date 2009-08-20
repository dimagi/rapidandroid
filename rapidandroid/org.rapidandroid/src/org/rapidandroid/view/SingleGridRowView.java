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

package org.rapidandroid.view;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils.TruncateAt;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 27, 2009 Summary:
 */
public class SingleGridRowView extends TableLayout {

	private int mColCount;

	TableRow mRow;
	// TextView mMessageIDCol;
	// TextView mMonitorCol;
	Vector<TextView> mDataCols;
	boolean isOdd = false;
	private int mColWidth;
	private static final int DATA_COLUMN_OFFSET = 2;

	/**
	 * @param context
	 */
	public SingleGridRowView(Context context, Cursor c, int colwidth) {
		super(context);
		mColWidth = colwidth;

		mColCount = c.getColumnCount() - 5;

		mRow = new TableRow(context);

		isOdd = (c.getPosition() % 2) == 1;
		if (isOdd) {
			// mRow.setBackgroundColor(org.rapidandroid.R.color.background_gray);
		} else {
			// mRow.setBackgroundColor(android.R.color.background_dark);
		}
		// mMessageIDCol = new TextView(context);
		// mMessageIDCol.setGravity(Gravity.LEFT);
		// mMessageIDCol.setWidth(getWidth()/mColCount);
		// mMessageIDCol.setPadding(3, 3, 3, 3);
		// mRow.addView(mMessageIDCol, itemCounter++);

		// mMonitorCol = new TextView(context);
		// mMonitorCol.setGravity(Gravity.LEFT);
		// mMonitorCol.setPadding(3, 3, 3, 3);

		// mMonitorCol.setWidth(getWidth()/mColCount);
		// mRow.addView(mMonitorCol, itemCounter++);

		mDataCols = new Vector<TextView>();

		for (int i = 0; i < mColCount; i++) {
			TextView coldata = new TextView(getContext());
			coldata.setSingleLine(); // no wrapping bab!
			coldata.setEllipsize(TruncateAt.END); // makeit ellipsize instead of
													// spillage!!!
			coldata.setWidth(mColWidth);
			// coldata.setBackgroundColor(Color.TRANSPARENT);

			coldata.setPadding(0, 4, 0, 4);

			mDataCols.add(coldata);
			mRow.addView(coldata, i);
		}

		// mRow.setWeightSum(mColCount);
		addView(mRow);
		setData(c);
	}

	public void setData(Cursor c) {

		for (int i = 0; i < mColCount; i++) {
			TextView coldata = mDataCols.get(i);
			if (c.getString(i + DATA_COLUMN_OFFSET).equals("")) {
				coldata.setText("(null)");
			} else {
				coldata.setText(c.getString(i + DATA_COLUMN_OFFSET));
			}
		}
	}

}

package org.rapidandroid.view;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 27, 2009
 * Summary:
 */
public class SingleRowView extends TableLayout {

	private int mColCount;
	
	TableRow mRow;
	TextView mMessageIDCol;
	TextView mMonitorCol;
	Vector<TextView> mDataCols;
	
	/**
	 * @param context
	 */
	public SingleRowView(Context context, Cursor c) {
		super(context);
		int itemCounter = 0; 

		mRow = new TableRow(context);		
		
		mMessageIDCol = new TextView(context);
		mMessageIDCol.setGravity(1);
		mMessageIDCol.setPadding(3, 3, 3, 3);
		mRow.addView(mMessageIDCol, itemCounter++);
		
		mMonitorCol = new TextView(context);
		mMonitorCol.setGravity(1);
		mMonitorCol.setPadding(3, 3, 3, 3);
		mRow.addView(mMonitorCol, itemCounter++);
		
		mDataCols = new Vector<TextView>();
		mColCount =  c.getColumnCount();
		for(int i = 0; i < mColCount -2; i++) {
			TextView coldata = new TextView(getContext());
			coldata.setPadding(3, 3, 3, 3);
			coldata.setGravity(1);
			//coldata.setText("null");
			mDataCols.add(coldata);
			
			mRow.addView(coldata, itemCounter++);
		}
		
		mRow.setWeightSum(itemCounter);
		this.addView(mRow);
		setData(c);
	}
	
	public void setData(Cursor c) {				
		mMessageIDCol.setText(c.getString(0) + " | ");
		mMonitorCol.setText(c.getString(1) + " | ");
		
		String suffix = " | ";
		for(int i = 0; i < mColCount - 2; i++) {
			if(i == mColCount - 3) {
				suffix = "";
			}
			TextView coldata = mDataCols.get(i);
			if(c.getString(i+2) == "") {
				coldata.setText("(null)" + suffix);
			} else {
				coldata.setText(c.getString(i+2) + suffix);
			}
		}		
	}

}

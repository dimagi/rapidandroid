package org.rapidandroid.view;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
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
	boolean isOdd = false;
	
	/**
	 * @param context
	 */
	public SingleRowView(Context context, Cursor c) {
		super(context);
		int itemCounter = 0; 
		
		mColCount =  c.getColumnCount();
		
		mRow = new TableRow(context);
		mRow.setGravity(Gravity.LEFT);
		isOdd = (c.getPosition() % 2) == 1 ;
		if(isOdd) {
			mRow.setBackgroundColor(android.R.color.background_light);
		} else {
			mRow.setBackgroundColor(android.R.color.background_dark);
		}
		mMessageIDCol = new TextView(context);
		//mMessageIDCol.setGravity(Gravity.LEFT);
		//mMessageIDCol.setWidth(getWidth()/mColCount);
		mMessageIDCol.setPadding(3, 3, 3, 3);
		mRow.addView(mMessageIDCol, itemCounter++);
		
		mMonitorCol = new TextView(context);
		//mMonitorCol.setGravity(Gravity.LEFT);
		mMonitorCol.setPadding(3, 3, 3, 3);
		
		//mMonitorCol.setWidth(getWidth()/mColCount);
		mRow.addView(mMonitorCol, itemCounter++);
		
		mDataCols = new Vector<TextView>();
		
		for(int i = 0; i < mColCount -2; i++) {
			TextView coldata = new TextView(getContext());
			coldata.setPadding(3, 3, 3, 3);
			//coldata.setWidth(getWidth()/mColCount);
			//coldata.setGravity(Gravity.LEFT);
			//coldata.setText("null");
			mDataCols.add(coldata);
			
			mRow.addView(coldata, itemCounter++);
		}
		
		//mRow.setWeightSum(itemCounter);
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

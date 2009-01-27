package org.rapidandroid.view;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
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
		mRow.addView(mMessageIDCol, itemCounter++);
		
		mMonitorCol = new TextView(context);
		mRow.addView(mMonitorCol, itemCounter++);
		
		mDataCols = new Vector<TextView>();
		mColCount =  c.getColumnCount();
		for(int i = 0; i < mColCount -2; i++) {
			TextView coldata = new TextView(getContext());
			coldata.setText("null");
			mDataCols.add(coldata);
			mRow.addView(coldata, itemCounter++);
		}
		this.addView(mRow);
		setData(c);
	}
	
	public void setData(Cursor c) {				
		mMessageIDCol.setText(c.getString(0));
		mMonitorCol.setText(c.getString(1));
		
		for(int i = 0; i < mColCount -2; i++) {
			TextView coldata = mDataCols.get(i);
			coldata.setText(c.getString(i+2));					
		}		
	}

}

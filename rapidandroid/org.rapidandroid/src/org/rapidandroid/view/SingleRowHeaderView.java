package org.rapidandroid.view;

import java.util.Vector;

import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;

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
public class SingleRowHeaderView extends TableLayout {
private int mColCount;
	
	TableRow mRow;
	TextView mMessageIDCol;
	TextView mMonitorCol;
	Vector<TextView> mDataCols;
	
	/**
	 * @param context
	 */
	public SingleRowHeaderView(Context context, Form f) {
		super(context);
		int itemCounter = 0; 

		mRow = new TableRow(context);		
		mMessageIDCol = new TextView(context);
		mMessageIDCol.setText("ID |");
		mRow.addView(mMessageIDCol, itemCounter++);
		
		mMonitorCol = new TextView(context);
		mMonitorCol.setText(" Mon | ");
		mRow.addView(mMonitorCol, itemCounter++);
		
		
		mDataCols = new Vector<TextView>();
		mColCount =  f.getFields().length;
		Field[] fields = f.getFields();
		String suffix = " | ";
		for(int i = 0; i < mColCount; i++) {
			if(i == mColCount-1) {
				suffix = "";
			}
			TextView coldata = new TextView(getContext());
			coldata.setText(fields[i].getName() + suffix);
			
			mDataCols.add(coldata);
			mRow.addView(coldata, itemCounter++);
		}
		
	}
}

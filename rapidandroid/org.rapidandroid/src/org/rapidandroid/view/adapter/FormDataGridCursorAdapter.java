package org.rapidandroid.view.adapter;


import org.rapidandroid.view.SingleGridRowView;
import org.rapidsms.java.core.model.Form;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CursorAdapter;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 27, 2009
 * Summary:
 */
public class FormDataGridCursorAdapter extends CursorAdapter {

	Form mForm;
	Context mContext;
	int mWidth;
	int mColWidth;
	
	/**
	 * @param context
	 * @param c
	 */
	public FormDataGridCursorAdapter(Context context, Form form, Cursor c, int screenWidth) {
		super(context, c);
		mForm = form;	
		mContext = context;
		mWidth = screenWidth;
		mColWidth = screenWidth/(c.getColumnCount()-5);
		
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if(view != null) {
			SingleGridRowView srv = (SingleGridRowView)view;
			srv.setData(cursor);
		}
					
	}
	
	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		SingleGridRowView srv = new SingleGridRowView(context, cursor, mColWidth);			
		return srv;		
	}

	

	

}

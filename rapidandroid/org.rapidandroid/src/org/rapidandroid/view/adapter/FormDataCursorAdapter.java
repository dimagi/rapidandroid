package org.rapidandroid.view.adapter;

import org.rapidandroid.view.ParsedMessageView;
import org.rapidandroid.view.SingleRowView;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.parser.IParseResult;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 27, 2009
 * Summary:
 */
public class FormDataCursorAdapter extends CursorAdapter {

	Form mForm;
	Context mContext;
	
	/**
	 * @param context
	 * @param c
	 */
	public FormDataCursorAdapter(Context context, Form form, Cursor c) {
		super(context, c);
		mForm = form;	
		mContext = context;
		
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if(view != null) {
			SingleRowView srv = (SingleRowView)view;
			srv.setData(cursor);
		}
					
	}
	
	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		SingleRowView srv = new SingleRowView(context, cursor);			
		return srv;		
	}

	

	

}

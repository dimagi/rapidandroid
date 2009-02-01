package org.rapidandroid.view.adapter;

import java.util.HashMap;

import org.rapidandroid.view.SummaryCursorView;
import org.rapidsms.java.core.model.Form;

import android.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009 Summary:
 */
public class SummaryCursorAdapter extends CursorAdapter {

	private int mLoadViewCount = 0;
	
	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
		mLoadViewCount = 0;
	}

	@Override
	public void changeCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		super.changeCursor(cursor);
		mLoadViewCount = 0;
	}
	
	@Override
	protected void init(Context context, Cursor c, boolean autoRequery) {
		// TODO Auto-generated method stub
		super.init(context, c, autoRequery);
		mLoadViewCount = 0;
	}

	private Form mForm;
	String[] mFields;

	private static Boolean bFalse = Boolean.valueOf(false);
	private static Boolean bTrue = Boolean.valueOf(true);

	private HashMap<Integer, Boolean> mExpanded;

	/**
	 * @param context
	 * @param c
	 */
	public SummaryCursorAdapter(Context context, Cursor c, Form f) {
		super(context, c, false);
		mForm = f;
		mFields = new String[mForm.getFields().length];
		for (int i = 0; i < mFields.length; i++) {
			mFields[i] = mForm.getFields()[i].getName();
		}
		mExpanded = new HashMap<Integer, Boolean>();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#bindView(android.view.View,
	 * android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		if(mLoadViewCount < 10) {
		
		} else {
		
		}
		
		SummaryCursorView pmcv = (SummaryCursorView) view;
		pmcv.setData(cursor);
		Integer intpos = Integer.valueOf(cursor.getPosition());
		if (!mExpanded.containsKey(intpos)) {
			mExpanded.put(intpos, bFalse);
		}
		pmcv.setExpanded(mExpanded.get(intpos).booleanValue());

		mLoadViewCount++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.CursorAdapter#newView(android.content.Context,
	 * android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Integer intpos = Integer.valueOf(cursor.getPosition());
		mExpanded.put(intpos, bFalse);
		return new SummaryCursorView(context, cursor, mFields, false);
	}

	public void toggle(int position) {
		Integer intpos = Integer.valueOf(position);
		if (!mExpanded.containsKey(intpos)) {
			mExpanded.put(intpos, bTrue);
		}
		mExpanded.put(intpos, Boolean.valueOf(!mExpanded.get(intpos).booleanValue()));
		notifyDataSetChanged();
	}

}

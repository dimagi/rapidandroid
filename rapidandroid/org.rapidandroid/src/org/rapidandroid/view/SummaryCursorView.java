package org.rapidandroid.view;

import java.text.ParseException;
import java.util.Date;

import org.rapidandroid.R;
import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.parser.IParseResult;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009
 * Summary:
 */
public class SummaryCursorView extends TableLayout {

	
	String[] mFields;
	
	private int mColTime = -1;
	private int mColMessage = -2;
	private int mColPhone = -2;
	
	//TableRow mMessageSummaryRow;
	TextView mMessageSummary;
	TextView mMonitorString;

	TextView mRawMessageRow;
//	TableRow mParsedSummaryRow;
//TextView mParsedSummaryRow;
	
	TableRow[] mParsedDataRows;	
	TextView[] mFieldLabels;
	TextView[] mFieldValues;
	
	/**
	 * @param context
	 */
	public SummaryCursorView(Context context, Cursor formDataCursor, String[] fields, boolean expanded) {
		super(context);
		// 
		
		mColMessage  = formDataCursor.getColumnCount() -3;
		mColTime  = formDataCursor.getColumnCount() -2;
		mColPhone  = formDataCursor.getColumnCount() -1;
			
		mFields = fields;
		//*************
		TableRow topRow = new TableRow(getContext());
		//First row, summary & sender		
		mMessageSummary = new TextView(getContext());    
		mMessageSummary.setPadding(3, 3, 3, 3);
		mMessageSummary.setTextSize(16);
		mMessageSummary.setGravity(Gravity.LEFT);
		
        //addView(mMessageSummary, new TableLayout.LayoutParams());
		topRow.addView(mMessageSummary);
				
		mMonitorString = new TextView(getContext());
		mMonitorString.setPadding(3, 3, 8, 3);
		mMonitorString.setTextSize(16);
		mMonitorString.setGravity(Gravity.RIGHT);
		
		topRow.addView(mMonitorString);
		addView(topRow);
		 //***********
        //Third row, actual message        
		mRawMessageRow = new TextView(getContext());
		mRawMessageRow.setPadding(2, 2, 8, 2);
		mRawMessageRow.setTextSize(12);
		//mRawMessageRow.setBackgroundColor(R.color.solid_green);
		// addView(mRawMessageRow, new TableLayout.LayoutParams());
		addView(mRawMessageRow);
		
		
		
		//*************
//		//First row, parsed data
//		mParsedSummaryRow = new TextView(getContext());        
//		mParsedSummaryRow.setPadding(2, 2, 2, 2);
//		mParsedSummaryRow.setTextSize(16);               
//        //this.addView(mParsedSummaryRow, new TableLayout.LayoutParams());
//		addView(mParsedSummaryRow);

		int lenresults = fields.length;
		mParsedDataRows = new TableRow[lenresults];
		mFieldLabels = new TextView[lenresults];
		mFieldValues = new TextView[lenresults];
		

		for (int i = 0; i < lenresults; i++) {
			TableRow row = new TableRow(getContext());
//			row.setBackgroundColor(R.color.background_red);
//			row.setDrawingCacheBackgroundColor(R.color.background_red);
			//row.setBackgroundResource(android.R.drawable.)
			
			TextView txvFieldName = new TextView(getContext());			
			txvFieldName.setTextSize(14);
//			txvFieldName.setBackgroundColor(R.color.background_red);
//			txvFieldName.setDrawingCacheBackgroundColor(R.color.background_red);
			txvFieldName.setGravity(Gravity.LEFT);
			txvFieldName.setPadding(10, 2, 2, 2);
			mFieldLabels[i] = txvFieldName;
			
			TextView txvFieldData = new TextView(getContext());
			txvFieldData.setGravity(Gravity.LEFT);
			txvFieldData.setTextSize(14);	
			txvFieldData.setPadding(2, 2, 2, 2);
			//txvFieldData.setBackgroundColor(R.color.background_red);
			mFieldValues[i] = txvFieldData;
			
			
			row.addView(txvFieldName, 0);
			row.addView(txvFieldData, 1);
			mParsedDataRows[i] = row;
			
			this.addView(row, new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
		}
		
		this.setColumnStretchable(0, true);
		this.setColumnStretchable(1, true);
		//this.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		setMessageTop(formDataCursor);
		setParsedBottom(formDataCursor);
		setExpanded(expanded);
	}
	
	private void setMessageTop(Cursor cr) {
		Date msgDate;
		try {
			msgDate = Message.SQLDateFormatter.parse(cr.getString(mColTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgDate = new Date();
		}
		mMessageSummary.setText(Message.DisplayDateTimeFormat.format(msgDate));
		mRawMessageRow.setText(cr.getString(mColMessage));
		mMonitorString.setText(cr.getString(mColPhone));
//		mMsg = MessageTranslator.GetMessage(getContext(), cr.getInt(1));
//        mMessageSummary.setText(Message.DisplayDateTimeFormat.format(mMsg.getTimestamp()));          
//        if(mMsg.getMonitor() == null) {
//        	mMonitorString.setText("null");
//        } else {
//        	mMonitorString.setText("  " + mMsg.getMonitor().getPhone());
//        }
//        mRawMessageRow.setText(mMsg.getMessageText());        
	}
	
	private void setParsedBottom(Cursor cr) {		
		//mParsedSummaryRow.setText("Parsed Data");
        int lenresults = mFields.length;        
        for(int i = 0; i < lenresults; i++) {        	
        	mFieldLabels[i].setText(mFields[i]);        	
        	mFieldValues[i].setText(cr.getString(i+2));        	
        }        	
	}	
	
	public void setData(Cursor cr) {
		setMessageTop(cr);
		setParsedBottom(cr);
	}
	
	public void setExpanded(boolean expanded) {		
		mRawMessageRow.setVisibility(expanded ? VISIBLE : GONE);
		//mParsedSummaryRow.setVisibility(expanded ? VISIBLE : GONE);
		int rowLen = mParsedDataRows.length;
		for(int i = 0; i < rowLen; i++) {
			mParsedDataRows[i].setVisibility(expanded ? VISIBLE : GONE);
		}			
	}	

}

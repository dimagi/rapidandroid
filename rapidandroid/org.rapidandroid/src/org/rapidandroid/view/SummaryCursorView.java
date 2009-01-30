package org.rapidandroid.view;

import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.parser.IParseResult;

import android.content.Context;
import android.database.Cursor;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009
 * Summary:
 */
public class SummaryCursorView extends TableLayout {

	Message mMsg;
	String[] mFields;
	
	//TableRow mMessageSummaryRow;
	TextView mMessageSummary;
	TextView mMonitorString;

	TextView mRawMessageRow;
//	TableRow mParsedSummaryRow;
	TextView mParsedSummaryRow;
	
	TableRow[] mParsedDataRows;	
	TextView[] mFieldLabels;
	TextView[] mFieldValues;
	
	/**
	 * @param context
	 */
	public SummaryCursorView(Context context, Cursor formDataCursor, String[] fields, boolean expanded) {
		super(context);
		// 
			
		mFields = fields;
		//*************
		//First row, summary		
		mMessageSummary = new TextView(getContext());    
		mMessageSummary.setPadding(2, 2, 2, 2);
		mMessageSummary.setTextSize(16);
        //addView(mMessageSummary, new TableLayout.LayoutParams());
		addView(mMessageSummary);
		
		
		 //***********
        //Second row, sender info
		mMonitorString = new TextView(getContext());
		mMonitorString.setPadding(1, 1, 1, 1);
		mMonitorString.setTextSize(14);
		addView(mMonitorString);
		
		 //***********
        //Third row, actual message        
		mRawMessageRow = new TextView(getContext());
		mRawMessageRow.setPadding(1, 1, 1, 1);
		mRawMessageRow.setTextSize(12);
		// addView(mRawMessageRow, new TableLayout.LayoutParams());
		addView(mRawMessageRow);
		
		
		//*************
		//First row, parsed data
		mParsedSummaryRow = new TextView(getContext());        
		mParsedSummaryRow.setPadding(2, 2, 2, 2);
		mParsedSummaryRow.setTextSize(16);               
        //this.addView(mParsedSummaryRow, new TableLayout.LayoutParams());
		addView(mParsedSummaryRow);

		int lenresults = fields.length;
		mParsedDataRows = new TableRow[lenresults];
		mFieldLabels = new TextView[lenresults];
		mFieldValues = new TextView[lenresults];

		for (int i = 0; i < lenresults; i++) {
			TableRow row = new TableRow(getContext());
			TextView txvFieldName = new TextView(getContext());			
			txvFieldName.setTextSize(14);
			mFieldLabels[i] = txvFieldName;
			
			TextView txvFieldData = new TextView(getContext());
			txvFieldData.setTextSize(14);			
			mFieldValues[i] = txvFieldData;
			
			row.addView(txvFieldName, 0);
			row.addView(txvFieldData, 1);
			mParsedDataRows[i] = row;
			
			this.addView(row, new TableLayout.LayoutParams());
		}
		
		setMessageTop(formDataCursor);
		setParsedBottom(formDataCursor);
		setExpanded(expanded);
	}
	
	private void setMessageTop(Cursor cr) {
		mMsg = MessageTranslator.GetMessage(getContext(), cr.getInt(1));
        mMessageSummary.setText("ID: " + mMsg.getID() + " :: " + Message.DisplayDateFormat.format(mMsg.getTimestamp()));          
        mMonitorString.setText(mMsg.getMonitor().getPhone());       
        mRawMessageRow.setText(mMsg.getMessageText());        
	}
	
	private void setParsedBottom(Cursor cr) {		
		mParsedSummaryRow.setText("Parsed Data");
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
		mParsedSummaryRow.setVisibility(expanded ? VISIBLE : GONE);
		int rowLen = mParsedDataRows.length;
		for(int i = 0; i < rowLen; i++) {
			mParsedDataRows[i].setVisibility(expanded ? VISIBLE : GONE);
		}			
	}	

}

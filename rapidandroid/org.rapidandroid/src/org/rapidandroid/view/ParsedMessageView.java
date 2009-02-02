
package org.rapidandroid.view;


import java.text.SimpleDateFormat;



import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.parser.IParseResult;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 *  
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 9, 2009
 * @deprecated
 * 
 *   
 */

public class ParsedMessageView extends TableLayout {
	
	private Form mForm;
	
	//TableRow mMessageSummaryRow;
	TextView mMessageSummary;
	TextView mMonitorString;

	TextView mRawMessageRow;
//	TableRow mParsedSummaryRow;
	TextView mParsedSummaryRow;
	
	TableRow[] mParsedDataRows;	
	TextView[] mFieldLabels;
	TextView[] mFieldValues;
	
	public ParsedMessageView(Context context, Form form, Message mesg, IParseResult[] results, boolean expanded) { 
		super(context);
		mForm = form;

			
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
		
		
		setMessageTop(mesg);
		
		
		//*************
		//First row, parsed data
		mParsedSummaryRow = new TextView(getContext());        
		mParsedSummaryRow.setPadding(2, 2, 2, 2);
		mParsedSummaryRow.setTextSize(16);               
        //this.addView(mParsedSummaryRow, new TableLayout.LayoutParams());
		addView(mParsedSummaryRow);

		int lenresults = results.length;
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
			txvFieldData.setText(results[i].getValue().toString());
			mFieldValues[i] = txvFieldData;
			
			row.addView(txvFieldName, 0);
			row.addView(txvFieldData, 1);
			mParsedDataRows[i] = row;
			
			this.addView(row, new TableLayout.LayoutParams());
		}        	
		
		setParsedBottom(results);
	}
	
	
	
	private void setMessageTop(Message mesg) {		        
        mMessageSummary.setText("ID: " + mesg.getID() + " :: " + Message.DisplayDateTimeFormat.format(mesg.getTimestamp()));          
        mMonitorString.setText(mesg.getMonitor().getPhone());       
        mRawMessageRow.setText(mesg.getMessageText());        
	}
	
	private void setParsedBottom(IParseResult[] results) {		
		mParsedSummaryRow.setText("Parsed Data");
        int lenresults = results.length;        
        for(int i = 0; i < lenresults; i++) {        	
        	mFieldLabels[i].setText(mForm.getFields()[i].getName());        	
        	mFieldValues[i].setText(results[i].getValue().toString());        	        	
        }        	
	}	
	
	public void setData(Message mesg, IParseResult[] results) {
		setMessageTop(mesg);
		setParsedBottom(results);
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

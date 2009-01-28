package org.rapidandroid.view.adapter;

import java.util.Date;

import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidsms.java.core.model.Monitor;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class MessageCursorAdapter extends CursorAdapter {

	public MessageCursorAdapter(Context context, Cursor c) {
		super(context, c);		
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if(view != null) {
			int MonitorID = cursor.getInt(2);
			String timestamp = cursor.getString(3);
			String message = cursor.getString(4);
			boolean isoutgoing = Boolean.parseBoolean(cursor.getString(4));
			Date hackDate = new Date();
			
			SimpleMessageView srv = (SimpleMessageView)view;
			srv.setData(message,hackDate,MonitorID,isoutgoing);
		}
					
	}
	
	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		int MonitorID = cursor.getInt(2);
		String timestamp = cursor.getString(3);
		String message = cursor.getString(4);
		boolean isoutgoing = Boolean.parseBoolean(cursor.getString(4));
		Date hackDate = new Date();
		
		SimpleMessageView srv = new SimpleMessageView(context, message,hackDate,MonitorID,isoutgoing);			
		return srv;		
	}
	
	
	private class SimpleMessageView extends TableLayout {

		private TextView txvMessage;
		private TextView txvHeader;
		
		
		public SimpleMessageView(Context context, String message, Date timestamp, int monitorID, boolean isOutgoing) {
			super(context);
			txvHeader = new TextView(context);
			txvHeader.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			//this.addView(txvHeader, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			this.addView(txvHeader);
			
			txvMessage = new TextView(context);
			//this.addView(txvMessage, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			this.addView(txvMessage);
			
			setData(message,timestamp,monitorID,isOutgoing);
			// TODO Auto-generated constructor stub
		}
		
		public void setData(String message, Date timestamp, int monitorID, boolean isOutgoing) {
			StringBuilder sb = new StringBuilder();
			if(isOutgoing) {
				sb.append("[Out] <<< ");
			} else {
				sb.append("[In] >>> " );
			}
			
			sb.append(timestamp.toString() + " ");
			Monitor m = MessageTranslator.GetMonitor(getContext(), monitorID);
			sb.append(m.getPhone());			
			txvHeader.setText(timestamp.toString());
			
			txvMessage.setText(message);
		}
		
	}

}

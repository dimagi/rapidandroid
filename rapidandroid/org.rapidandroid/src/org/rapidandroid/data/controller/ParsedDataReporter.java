package org.rapidandroid.data.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 30, 2009
 * 
 */
public class ParsedDataReporter {

	SmsDbHelper mHelper;
	Context mContext;
	
	private String[] messageColumns = new String[] {"message_time", "monitor_id", "monitor_phone", "message_text"};
	
	public ParsedDataReporter(Context context) {
		mHelper = new SmsDbHelper(context);
		mContext = context;
	}
	
	public void exportFormDataToCSV(Form f, Calendar startDate, Calendar endDate) {
		//mListviewCursor = getContentResolver().query(Uri.parse(RapidSmsDBConstants.FormData.CONTENT_URI_PREFIX + mChosenForm.getFormId()), null,null,null,"message_id desc");
		
//		what the frack, joins don't work in Android mysql.  This query works fine on the desktop mysql programs!
		//hackish workaround is to do the same query as the MessageView and have to query for the Message for the direct output
		StringBuilder query = new StringBuilder();
		query.append("select " + RapidSmsDBConstants.FormData.TABLE_PREFIX);
		query.append(f.getPrefix() + ".*");
		query.append(", rapidandroid_message.message,rapidandroid_message.time, rapidandroid_monitor._id as monitor_id, rapidandroid_monitor.phone as monitor_phone ");
		query.append(" from " + RapidSmsDBConstants.FormData.TABLE_PREFIX + f.getPrefix());
		query.append(" join rapidandroid_message on (");
		query.append(RapidSmsDBConstants.FormData.TABLE_PREFIX + f.getPrefix());
		query.append(".message_id = rapidandroid_message._id");
		query.append(") ");
		
		query.append(" join rapidandroid_monitor on (");
		query.append("rapidandroid_monitor._id = rapidandroid_message.monitor_id");
		query.append(") ");
		
		query.append("WHERE rapidandroid_message.time > '" + startDate.get(Calendar.YEAR) + "-" + (startDate.get(Calendar.MONTH)+1) + "-" + startDate.get(Calendar.DATE) + "' AND ");
		query.append(" rapidandroid_message.time < '" + endDate.get(Calendar.YEAR) + "-" + (1+endDate.get(Calendar.MONTH)) + "-" + endDate.get(Calendar.DATE) + "';");
		
		Cursor cr = mHelper.getReadableDatabase().rawQuery(query.toString(), null);
		//mListviewCursor = getContentResolver().query(RapidSmsDBConstants.Message.CONTENT_URI, null, null, null, "time DESC");
		//Cursor cr = mContext.getContentResolver().query(Uri.parse(RapidSmsDBConstants.FormData.CONTENT_URI_PREFIX + f.getFormId()), null,null,null,"message_id desc");
		File sdcard = Environment.getExternalStorageDirectory();
		File destinationdir = new File(sdcard,"rapidandroid/exports");
		destinationdir.mkdir();
		Date now = new Date();
		File destinationfile = new File(destinationdir,"formdata_" + f.getPrefix() + now.getYear() + now.getMonth() + now.getDate() + "-" + now.getHours() + now.getMinutes() + ".csv");
		FileOutputStream fOut = null;
		try {
			destinationfile.createNewFile();
			fOut =  new FileOutputStream(destinationfile); 
			String[] cols = cr.getColumnNames();
			int colcount = cols.length;
			StringBuilder sbrow = new StringBuilder();
			for(int i = 0; i < colcount; i++) {
				//sbrow.append(cols[i] + ",");
				sbrow.append(cols[i]);
				if (i < colcount - 1) {
					sbrow.append(",");
				} else {
					sbrow.append("\n");
				}
			}
			//add the auxiliary columns
//			int colcount2 =  messageColumns.length;
//			for(int i = 0; i < colcount2; i++) {
//				sbrow.append(messageColumns[i]);
//				if (i <= colcount2 - 1) {
//					sbrow.append(",");
//				} else {
//					sbrow.append("\n");
//				}
//			}
			fOut.write(sbrow.toString().getBytes());
			cr.moveToFirst();
			do{
				sbrow = new StringBuilder();
				for(int i = 0; i < colcount; i++) {
					//sbrow.append(cr.getString(i) + ",");
					sbrow.append(cr.getString(i));
					if (i < colcount - 1) {
						sbrow.append(",");
					} else {
						sbrow.append("\n");
					}
				}
//				//next, append the direct monitordata.
//				Message msg = MessageTranslator.GetMessage(mContext, cr.getInt(Message.COL_PARSED_MESSAGE_ID));
//				sbrow.append(Message.SQLDateFormatter.format(msg.getTimestamp()) + ",");
//				sbrow.append(msg.getMonitor().getID() + ",");
//				sbrow.append(msg.getMonitor().getPhone() + ",");
//				sbrow.append(msg.getMessageText() + "\n");				
				fOut.write(sbrow.toString().getBytes());
			} while (cr.moveToNext());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			cr.close();
			if(fOut != null) {
				try {
					fOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//select formdata_bednets.*, rapidandroid_message.message,rapidandroid_message.time from formdata_bednets
		//join rapidandroid_message on (formdata_bednets.message_id = rapidandroid_message._id)
	}
}

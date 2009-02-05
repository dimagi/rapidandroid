package org.rapidandroid.data.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.Constants;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 30, 2009
 * 
 */
public class ParsedDataReporter {

	private String[] messageColumns = new String[] {"message_time", "monitor_id", "monitor_phone", "message_text"};
	
	public synchronized static Date getOldestMessageDate(Context context, Form f) {
		SmsDbHelper mHelper = new SmsDbHelper(context);
		return getOldestMessageDate(mHelper, f);
		
	}
	
	public synchronized static Date getOldestMessageDate(SmsDbHelper mHelper, Form f) {
		// TODO Auto-generated method stub
		StringBuilder query = new StringBuilder();
		query.append("select min(rapidandroid_message.time) ");		
		query.append(" from " + RapidSmsDBConstants.FormData.TABLE_PREFIX + f.getPrefix());
		query.append(" join rapidandroid_message on (");
		query.append(RapidSmsDBConstants.FormData.TABLE_PREFIX + f.getPrefix());
		query.append(".message_id = rapidandroid_message._id");
		query.append(") ");
		
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cr = db.rawQuery(query.toString(), null);
		if(cr.getCount() == 0) {
			cr.close();
			db.close();
			mHelper.close();
			return Constants.NULLDATE;
			
		}
		cr.moveToFirst();
		String dateString = cr.getString(0);
		
		if (dateString == null) {
			cr.close();
			db.close();
			mHelper.close();
			return Constants.NULLDATE;
		}

		Date ret = new Date();
		try {
			ret = Message.SQLDateFormatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			try {
				if (cr != null) {
					cr.close();
				}
				if(db!=null) {
					db.close();
				}
				if (mHelper != null) {
					mHelper.close();
				}
			} catch (Exception ex2) {

			}
		}
		cr.close();
		db.close();
		mHelper.close();
		return ret;
	}

	public synchronized static void exportFormDataToCSV(Context context, Form f, Calendar startDate, Calendar endDate) {
		SmsDbHelper mHelper = new SmsDbHelper(context);
		//build the query
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
				fOut.write(sbrow.toString().getBytes());
			} while (cr.moveToNext());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			cr.close();
			mHelper.close();
			if(fOut != null) {
				try {
					fOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
			//compressFile(destinationfile);
		}
	
	}

	void compressFile(File rawFile) {
		FileInputStream fin = null;
		GZIPOutputStream gz = null;
		try {
			fin = new FileInputStream(rawFile);
			FileOutputStream fout = new FileOutputStream(rawFile
					.getAbsoluteFile()
					+ ".gz");
			gz = new GZIPOutputStream(fout);
			byte[] buf = new byte[4096];
			int readCount;
			while ((readCount = fin.read(buf)) != -1) {
				gz.write(buf, 0, readCount);
			}

		} catch (Exception ex) {
		} finally {
			// Close the BufferedInputStream
			try {
				if (fin != null)
					fin.close();
				if (gz != null)
					gz.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}
}

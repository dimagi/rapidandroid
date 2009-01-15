/**
 * 
 */
package org.rapidandroid.receiver;

import java.sql.Timestamp;
import java.util.Date;

import org.rapidandroid.data.RapidSmsDataDefs;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;


/**
 * @author dmyung
 * @created Jan 12, 2009
 */
public class SmsReceiver  extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	
	private void triggerAppLaunch(Context context, SmsMessage msg)
	{
//		Intent broadcast = new Intent("org.rapidandroid.Dashboard");
//		broadcast.putExtra("from", msg.getOriginatingAddress());		
//		broadcast.putExtra("body", msg.getMessageBody());
		//broadcast.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 	
		//context.startActivity(new Intent(broadcast));
		
//		Cursor c = getContentResolver().query(Sms.Inbox.CONTENT_URI, 
//				null, null, null, null);//.query(Sms.CONTENT_URI, null, null, null, 
//				null); 
	}
	
	private void insertMessageToContentProvider(Context context, SmsMessage mesg) {
		Uri writeMessageUri = RapidSmsDataDefs.Message.CONTENT_URI;
		
		ContentValues messageValues = new ContentValues();
		messageValues.put(RapidSmsDataDefs.Message.MESSAGE,mesg.getMessageBody());		
		messageValues.put(RapidSmsDataDefs.Message.PHONE,mesg.getOriginatingAddress());
		
		Timestamp ts = new Timestamp(mesg.getTimestampMillis());		//convert the timestamp to a datetime string
		messageValues.put(RapidSmsDataDefs.Message.TIME,ts.toString());
		messageValues.put(RapidSmsDataDefs.Message.IS_OUTGOING,false);
		context.getContentResolver().insert(writeMessageUri, messageValues);		
	}

	
	
	@Override
	//source: http://www.devx.com/wireless/Article/39495/1954
	public void onReceive(Context context, Intent intent) {
		if(!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {//		{
			
			return;
		}
		SmsMessage msgs[] = getMessagesFromIntent(intent);
		
		
		for(int i=0; i < msgs.length; i++)
		{
			String message = msgs[i].getDisplayMessageBody();
			
			if(message != null && message.length() > 0)
			{
				Log.i("MessageListener:",  message);
				
//				//Our trigger message must be generic and human redable because it will end up
//				//In the SMS inbox of the phone.
//				if(message.startsWith("dimagi"))
//				{
//					triggerAppLaunch(context, msgs[i]);
//				}		
				
				insertMessageToContentProvider(context, msgs[i]);
			}
		}

		
	}

	//source: http://www.devx.com/wireless/Article/39495/1954
	private SmsMessage[] getMessagesFromIntent(Intent intent)
	{
		SmsMessage retMsgs[] = null;
		Bundle bdl = intent.getExtras();
		try{
			Object pdus[] = (Object [])bdl.get("pdus");
			retMsgs = new SmsMessage[pdus.length];
			for(int n=0; n < pdus.length; n++)
			{
				byte[] byteData = (byte[])pdus[n];
				retMsgs[n] = SmsMessage.createFromPdu(byteData);
			}	
			
		}catch(Exception e)
		{
			Log.e("GetMessages", "fail", e);
		}
		return retMsgs;
	}

}

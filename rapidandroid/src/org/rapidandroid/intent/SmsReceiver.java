/**
 * 
 */
package org.rapidandroid.intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

	
	
	@Override
	//source: http://www.devx.com/wireless/Article/39495/1954
	public void onReceive(Context context, Intent intent) {
		if(!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {//		{
			
			return;
		}
		SmsMessage msg[] = getMessagesFromIntent(intent);
		
		
		for(int i=0; i < msg.length; i++)
		{
			String message = msg[i].getDisplayMessageBody();
			
			if(message != null && message.length() > 0)
			{
				Log.i("MessageListener:",  message);
				
				//Our trigger message must be generic and human redable because it will end up
				//In the SMS inbox of the phone.
				if(message.startsWith("dimagi"))
				{
					triggerAppLaunch(context, msg[i]);
				}				
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

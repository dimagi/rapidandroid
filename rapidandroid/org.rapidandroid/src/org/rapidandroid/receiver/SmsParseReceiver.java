/**
 * 
 */
package org.rapidandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 *          Second level broadcast receiver. The idea is upon a successful SMS
 *          message save, a separate receiver will be triggered to handle the
 *          actual parsing and processing of the message.
 * 
 */
public class SmsParseReceiver extends BroadcastReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String body = intent.getStringExtra("body");
		body.length();
	}
}

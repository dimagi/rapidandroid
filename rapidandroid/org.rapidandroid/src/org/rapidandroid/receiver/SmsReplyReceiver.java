/*
 * Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package org.rapidandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Feb 2, 2009 Summary:
 */
public class SmsReplyReceiver extends BroadcastReceiver {

	public static final String KEY_DESTINATION_PHONE = "tophone";
	public static final String KEY_MESSAGE = "msg";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		SmsManager smgr = SmsManager.getDefault();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.containsKey(KEY_DESTINATION_PHONE) && extras.containsKey(KEY_MESSAGE)) {
				String destinationAddr = extras.getString(KEY_DESTINATION_PHONE);
				String mesg = extras.getString(KEY_MESSAGE);
				smgr.sendTextMessage(destinationAddr, null, mesg, null, null);
			}
		}
	}

}

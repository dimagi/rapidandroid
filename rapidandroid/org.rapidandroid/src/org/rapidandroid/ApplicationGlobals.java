package org.rapidandroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.rapidandroid.receiver.SmsParseReceiver;

import android.content.Context;
import android.util.Log;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Feb 10, 2009
 * Summary:
 */
public class ApplicationGlobals {

	private static boolean globalsLoaded = false;
	
	private static boolean mActive = false; 
	private static boolean mReplyParse = false;
	private static boolean mReplyFail = false;
	
	private static String mReplyParseText = "";
	private static String mReplyFailText = "";
	
	
	public static void initGlobals(Context context) {
		if(!globalsLoaded) {
			JSONObject globals = ApplicationGlobals.loadSettingsFromFile(context);
			try {
				
				if(globals.has(KEY_ACTIVE_ALL)) {
					mActive = globals.getBoolean(KEY_ACTIVE_ALL);
					
				} else {
					mActive = false;
				}
				mReplyParse = globals.getBoolean(KEY_PARSE_REPLY);
				mReplyFail = globals.getBoolean(KEY_FAILED_REPLY);
				mReplyParseText = globals.getString(KEY_PARSE_REPLY_TEXT);
				mReplyFailText = globals.getString(KEY_FAILED_REPLY_TEXT);
				globalsLoaded = true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static boolean doReplyOnFail() {
		if(mActive) {
			return mReplyFail;
		} else {
			return false;
		}
	}
	
	public static boolean doReplyOnParse() {
		if(mActive) {
			return mReplyParse;
		} else {
			return false;
		}
	}
	
	public static String getParseSuccessText() {
		return mReplyParseText;
	}
	
	public static String getParseFailText() {
		return mReplyFailText;
	}
	
	
	public static void checkGlobals(Context context) {		
		File f = context.getFileStreamPath(SETTINGS_FILE);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			saveGlobalSettings(context, false, false, "Message parsed successfully, thank you", false, "Unable to understand your message, please try again");
			
		}
	}
	
	/**
	 * 
	 */
	public static final String KEY_ACTIVE_ALL = "ActivateAll";
	
	
	/**
	 * 
	 */
	public static final String KEY_FAILED_REPLY_TEXT = "FailedReplyText";
	/**
	 * 
	 */
	public static final String KEY_FAILED_REPLY = "FailedReply";
	/**
	 * 
	 */
	public static final String KEY_PARSE_REPLY_TEXT = "ParseReplyText";
	/**
	 * 
	 */
	public static final String KEY_PARSE_REPLY = "ParseReply";
	
	/**
	 * 
	 */
	public static final String SETTINGS_FILE = "GlobalSettings.json";
	
	
	public static final String LOG_DEBUG_KEY = "ApplicationGlobals";
	/**
	 * 
	 */
	public static JSONObject loadSettingsFromFile(Context context) {
		FileInputStream fin = null;
		InputStreamReader irdr = null;
		JSONObject readobject = null;
		try {			

			fin = context.openFileInput(SETTINGS_FILE);

			irdr = new InputStreamReader(fin); // promote

			int size = (int) fin.getChannel().size();
			char[] data = new char[size]; // allocate char array of right
			// size
			irdr.read(data, 0, size); // read into char array
			irdr.close();

			String contents = new String(data);
			readobject = new JSONObject(contents);
			
			if(!readobject.has(KEY_ACTIVE_ALL)) {
				//dmyung hack to keep compatability with new version
				readobject.put(KEY_ACTIVE_ALL, false);
			}
			
//			mParseCheckbox.setChecked(readobject.getBoolean(KEY_PARSE_REPLY));
//			mParseReplyText.setText(readobject.getString(KEY_PARSE_REPLY_TEXT));
//			mNoparseCheckBox.setChecked(readobject.getBoolean(KEY_FAILED_REPLY));
//			mNoparseReplyText.setText(readobject.getString(KEY_FAILED_REPLY_TEXT));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (irdr != null) {
					irdr.close();
				}
				if (fin != null) {
					fin.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return readobject;
		}
	}
	
	/**
	 * 
	 */
	public static void saveGlobalSettings(Context context,boolean activateAll, boolean parseReply, String parseReplyText, boolean failedReply, String failedReplyText) {		
		JSONObject settingsObj = new JSONObject();
		FileOutputStream fos = null;
		try {
			settingsObj.put(KEY_ACTIVE_ALL, activateAll);
			settingsObj.put(KEY_PARSE_REPLY, parseReply);
			settingsObj.put(KEY_PARSE_REPLY_TEXT, parseReplyText);
			settingsObj.put(KEY_FAILED_REPLY, failedReply);
			settingsObj.put(KEY_FAILED_REPLY_TEXT, failedReplyText);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		try {
			fos = context.openFileOutput(SETTINGS_FILE, Context.MODE_PRIVATE);
			fos.write(settingsObj.toString().getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(LOG_DEBUG_KEY, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(LOG_DEBUG_KEY, e.getMessage());
		}
		finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			globalsLoaded = false;
			ApplicationGlobals.initGlobals(context);
		}
	}
}

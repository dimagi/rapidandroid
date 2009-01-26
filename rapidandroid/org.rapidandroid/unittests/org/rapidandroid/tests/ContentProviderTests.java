/**
 * 
 */
package org.rapidandroid.tests;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.apache.http.impl.cookie.DateUtils;
import org.rapidandroid.content.RapidSmsContentProvider;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.data.RapidSmsDataDefs;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.SimpleFieldType;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.service.ParsingService;
import org.rapidsms.java.core.parser.service.ParsingService.ParserType;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.mock.MockContentResolver;
import android.util.Log;
/**
 *  
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 14, 2009
 * Description:  Main unit test to verify functionality of the RapidSmsContentProviders * 
 *   
 */

public class ContentProviderTests extends
		android.test.ProviderTestCase<RapidSmsContentProvider> {

	private RapidSmsContentProvider mProv;
	private Uri currUri;
	
	public ContentProviderTests() {
		super(RapidSmsContentProvider.class,
				"org.rapidandroid.rapidandroiddata");
	}

	protected void setUp() throws Exception {
		super.setUp();
		mProv = getProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		//System.out.println("Teardown: Clear Message tables: " +	mProv.delete(RapidSmsDataDefs.Message.CONTENT_URI, null,null));
		//System.out.println("Teardown: Clear Message tables: " +	mProv.delete(RapidSmsDataDefs.Monitor.CONTENT_URI, null,null));
		
	}

	public void testSimple() {
		assertFalse(false);
	}
	
	
	
	

	public void test001ProviderGetTypeBase() {
		assertEquals(mProv.getType(RapidSmsDataDefs.Message.CONTENT_URI),RapidSmsDataDefs.Message.CONTENT_TYPE);
		assertEquals(mProv.getType(RapidSmsDataDefs.Monitor.CONTENT_URI),RapidSmsDataDefs.Monitor.CONTENT_TYPE);		
		
		assertEquals(mProv.getType(RapidSmsDataDefs.Form.CONTENT_URI),RapidSmsDataDefs.Form.CONTENT_TYPE);
		assertEquals(mProv.getType(RapidSmsDataDefs.Field.CONTENT_URI),RapidSmsDataDefs.Field.CONTENT_TYPE);		
		assertEquals(mProv.getType(RapidSmsDataDefs.FieldType.CONTENT_URI),RapidSmsDataDefs.FieldType.CONTENT_TYPE);
		
		//assertEquals(mProv.getType(RapidSmsDataDefs.FormData.CONTENT_URI),RapidSmsDataDefs.FormData.CONTENT_TYPE);	//this doesn';t exist in this case				
	}
	
	public void test001ProviderGetTypeID() {
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.Message.URI_PART + "/1")),RapidSmsDataDefs.Message.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.Monitor.URI_PART + "/1")),RapidSmsDataDefs.Monitor.CONTENT_ITEM_TYPE);		
		
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.Form.URI_PART + "/1")),RapidSmsDataDefs.Form.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.Field.URI_PART + "/1")),RapidSmsDataDefs.Field.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.FieldType.URI_PART + "/1")),RapidSmsDataDefs.FieldType.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.FormData.URI_PART + "/1")),RapidSmsDataDefs.FormData.CONTENT_TYPE);
	}	
	
	public void test002MonitorInsertAndQuerySingle() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDataDefs.Monitor.PHONE,"6176453236");		
		currUri = mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
		
		Cursor cr = mProv.query(currUri, null, null, null, null);
		assertEquals(1, cr.getCount());
		cr.close();
	}
	
	public void test002MonitorInsertRepeat() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDataDefs.Monitor.PHONE,"6176453236");		
		currUri = mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
				
		Uri repeatUri = mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
		
		assertEquals(repeatUri,currUri);
						
	}
	
	public void test002MonitorInsertAndVerifyCounts() {
		int count = 1;
		int baseline = 0;
		//Log.w("ContentProviderTests.testMonitorInsertAndVerifyCounts", "flasjdfklasdjf");
		Uri monitorquery = Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/mMonitorString");
		Cursor cr = mProv.query(monitorquery, null, null, null, null);
		baseline = cr.getCount();
		cr.close();
		
		for(int i = 0; i < count; i++) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(RapidSmsDataDefs.Monitor.PHONE,"8887" + i);		
			mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
		}
		
		Cursor cr2 =mProv.query(monitorquery, null, null, null, null);
		assertEquals(baseline+count, cr2.getCount());
		cr2.close();
        
	}
	
	String too_long = "Alert in golaoda werda no of otps only 1,other new 5 otps to start in plan.Problem in thise werda shortage of f100and75,traind manpowe,tranport do to thisez program for 1month stop but now already start it.";
	
	//add a bunch and confirm that the number of messages are ok
	//add messages as well and see if the number of 
	private void test002MessageInsertMessage() {
		
		String msg1 = "alert unlocked supply room at WSMA";
		String date1 = "10/30/2008 19:51";
		String phone1 = "251912149840";		
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN WKIG  23   0  0  0  PN OOZM  5  0  0  0 PN VHPF 5  0  0  0 PN XDQQ 6 0  0  0";
		date1 = "10/31/2008 11:50";
		phone1 = "251913086827";		
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN WKIG  23   0  0  0";
		date1 = "10/31/2008 11:51";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN OOZM  5  0  0  0";
		date1 = "10/31/2008 11:52";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN VHPF 5  0  0  0";
		date1 = "10/31/2008 11:53";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "Alert problem in meyu werda shortage of transport and pn because of this pn distrbution for 2wk stop but now started.In thise werda no of otps are 6.";
		date1 = "10/31/2008 11:58";
		phone1 = "251911877430";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "CANCEL TO  CHIROO WOREDA";
		date1 = "10/31/2008 12:58";
		phone1 = "251911877430";		
		doSendMessage(msg1, date1, phone1);
		
		
		//confirm the numbers
		Cursor msgcount = mProv.query(RapidSmsDataDefs.Message.CONTENT_URI, null, null, null, null);
		assertEquals(7,msgcount.getCount());
		msgcount.close();
		
		Cursor monitorcount = mProv.query(RapidSmsDataDefs.Monitor.CONTENT_URI, null, null, null, null);
		assertEquals(3,monitorcount.getCount());
		monitorcount.close();
	}
	
	private void doSendMessage(String msg, String date, String phone) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDataDefs.Message.MESSAGE,msg);		
		initialValues.put(RapidSmsDataDefs.Message.PHONE,phone);
		initialValues.put(RapidSmsDataDefs.Message.TIME,date);
		initialValues.put(RapidSmsDataDefs.Message.IS_OUTGOING,false);
		currUri = mProv.insert(RapidSmsDataDefs.Message.CONTENT_URI, initialValues);
	}
	
	private void test003InsertMessagesAndCountPerMonitor() {
		String msg1 = "alert unlocked supply room at WSMA";
		String date1 = "10/30/2008 19:51";
		String phone1 = "251912149840";		
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN WKIG  23   0  0  0  PN OOZM  5  0  0  0 PN VHPF 5  0  0  0 PN XDQQ 6 0  0  0";
		date1 = "10/31/2008 11:50";
		phone1 = "251913086827";		
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN WKIG  23   0  0  0";
		date1 = "10/31/2008 11:51";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN OOZM  5  0  0  0";
		date1 = "10/31/2008 11:52";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN VHPF 5  0  0  0";
		date1 = "10/31/2008 11:53";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "Alert problem in meyu werda shortage of transport and pn because of this pn distrbution for 2wk stop but now started.In thise werda no of otps are 6.";
		date1 = "10/31/2008 11:58";
		phone1 = "251911877430";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "CANCEL TO  CHIROO WOREDA";
		date1 = "10/31/2008 12:58";
		phone1 = "251911877430";		
		doSendMessage(msg1, date1, phone1);
		
		//251912149840 = 1
		//251913086827 = 4
		//251911877430 = 2
		//total should be 7
		
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251912149840"), 1);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251913086827"), 4);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251911877430"), 2);	
		
		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251912149840"));
		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251913086827"));
		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251911877430"));
		
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251912149840"),0);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251913086827"),0);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251911877430"),0);		
	}
	
	public void test004GetMesssages() {
		
	}
	
	private int getMonitorIdForPhoneNumber(String phonenumber) {
		Cursor monitor = mProv.query(RapidSmsDataDefs.Monitor.CONTENT_URI, null, "phone='" + phonenumber + "'", null,null);
		monitor.moveToFirst();
		assertEquals(1, monitor.getCount());
		int ret = monitor.getInt(0);
		monitor.close();
		return ret;
	}
	
	private void confirmMessageCountForMonitor(int monitor_id, int expected) {
		Uri getMessagesBySingleMonitor = ContentUris.withAppendedId(RapidSmsDataDefs.Monitor.MESSAGE_BY_MONITOR_URI, monitor_id);				
		Cursor msgcount = mProv.query(getMessagesBySingleMonitor, null, null, null, null);
		assertEquals(expected,msgcount.getCount());
		msgcount.close();
	}
	
	private void deleteMessagesByMonitor(int monitor_id) {
		Cursor msgcount = mProv.query(RapidSmsDataDefs.Message.CONTENT_URI, null, null, null, null);
		int oldcount = msgcount.getCount();
		msgcount.close();
		
		Uri deleteMessagesBySingleMonitor = ContentUris.withAppendedId(RapidSmsDataDefs.Monitor.MESSAGE_BY_MONITOR_URI, monitor_id);
		int delcount = mProv.delete(deleteMessagesBySingleMonitor, null, null);
		
		Cursor newcountc = mProv.query(RapidSmsDataDefs.Message.CONTENT_URI, null, null, null, null);
		int newcount = newcountc.getCount();
		newcountc.close();
		
		assertEquals(newcount+delcount,oldcount);
		//System.out.println("deleteMessagesByMonitor: " + monitor_id + " oldcount: " + oldcount + " delcount: " + delcount + " newcount: " + newcount);		 
	}
	
	
	
	
	
	public void testGetFieldTypes() {
		Uri query = RapidSmsDataDefs.FieldType.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
		cr.close();
	}	
	
	public void testGetFields() {
		Uri query = RapidSmsDataDefs.Field.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
		cr.close();
	}

	

	private void testGetFormData() {
		// objective:
		// get all the forms from the database
		Uri query = RapidSmsDataDefs.Form.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
		if (cr.getCount() > 0) {
			cr.moveToFirst();
			do {
				// using all the ids from the form
				// do queries off the tables in the database
				int formId = cr.getInt(0); // get the id
				Uri formDataUri = Uri
						.parse(RapidSmsDataDefs.FormData.CONTENT_URI_PREFIX
								+ formId);
				Cursor formDataCursor = mProv.query(formDataUri, null, null,
						null, null);

				formDataCursor.close();
			} while (cr.moveToNext() && cr.getCount() > 0);
		}
		cr.close();
	}
	
}

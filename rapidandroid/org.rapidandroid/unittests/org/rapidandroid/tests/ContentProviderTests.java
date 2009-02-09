/*
 *    rapidandroid - SMS gateway for the android platform
 *    Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
/**
 * 
 */
package org.rapidandroid.tests;

import org.rapidandroid.data.RapidSmsDBConstants;

import android.content.ContentUris;
import android.content.ContentValues;

import android.database.Cursor;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 14, 2009 Description: Main unit test to verify functionality of
 *          the RapidSmsContentProviders *
 * 
 */

public class ContentProviderTests extends AndroidTestCase {

	private Uri currUri;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		// System.out.println("Teardown: Clear Message tables: " +
		// mProv.delete(RapidSmsDBConstants.Message.CONTENT_URI, null,null));
		// System.out.println("Teardown: Clear Message tables: " +
		// mProv.delete(RapidSmsDBConstants.Monitor.CONTENT_URI, null,null));

	}

	public void testSimple() {
		assertFalse(false);
	}

	public void test001ProviderGetTypeBase() {
		assertEquals(getContext().getContentResolver().getType(RapidSmsDBConstants.Message.CONTENT_URI),
						RapidSmsDBConstants.Message.CONTENT_TYPE);
		assertEquals(getContext().getContentResolver().getType(RapidSmsDBConstants.Monitor.CONTENT_URI),
						RapidSmsDBConstants.Monitor.CONTENT_TYPE);

		assertEquals(getContext().getContentResolver().getType(RapidSmsDBConstants.Form.CONTENT_URI),
						RapidSmsDBConstants.Form.CONTENT_TYPE);
		assertEquals(getContext().getContentResolver().getType(RapidSmsDBConstants.Field.CONTENT_URI),
						RapidSmsDBConstants.Field.CONTENT_TYPE);
		assertEquals(getContext().getContentResolver().getType(RapidSmsDBConstants.FieldType.CONTENT_URI),
						RapidSmsDBConstants.FieldType.CONTENT_TYPE);

		// assertEquals(getContext().getContentResolver().getType(RapidSmsDBConstants.FormData.CONTENT_URI),RapidSmsDBConstants.FormData.CONTENT_TYPE);
		// //this doesn';t exist in this case
	}

	public void test001ProviderGetTypeID() {
		assertEquals(getContext().getContentResolver().getType(
																Uri.parse("content://" + RapidSmsDBConstants.AUTHORITY
																		+ "/" + RapidSmsDBConstants.Message.URI_PART
																		+ "/1")),
						RapidSmsDBConstants.Message.CONTENT_ITEM_TYPE);
		assertEquals(getContext().getContentResolver().getType(
																Uri.parse("content://" + RapidSmsDBConstants.AUTHORITY
																		+ "/" + RapidSmsDBConstants.Monitor.URI_PART
																		+ "/1")),
						RapidSmsDBConstants.Monitor.CONTENT_ITEM_TYPE);

		assertEquals(getContext().getContentResolver().getType(
																Uri.parse("content://" + RapidSmsDBConstants.AUTHORITY
																		+ "/" + RapidSmsDBConstants.Form.URI_PART
																		+ "/1")),
						RapidSmsDBConstants.Form.CONTENT_ITEM_TYPE);
		assertEquals(getContext().getContentResolver().getType(
																Uri.parse("content://" + RapidSmsDBConstants.AUTHORITY
																		+ "/" + RapidSmsDBConstants.Field.URI_PART
																		+ "/1")),
						RapidSmsDBConstants.Field.CONTENT_ITEM_TYPE);
		assertEquals(getContext().getContentResolver().getType(
																Uri.parse("content://" + RapidSmsDBConstants.AUTHORITY
																		+ "/" + RapidSmsDBConstants.FieldType.URI_PART
																		+ "/1")),
						RapidSmsDBConstants.FieldType.CONTENT_ITEM_TYPE);
		assertEquals(getContext().getContentResolver().getType(
																Uri.parse("content://" + RapidSmsDBConstants.AUTHORITY
																		+ "/" + RapidSmsDBConstants.FormData.URI_PART
																		+ "/1")),
						RapidSmsDBConstants.FormData.CONTENT_TYPE);
	}

	public void test002MonitorInsertAndQuerySingle() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDBConstants.Monitor.PHONE, "6176453236");
		currUri = getContext().getContentResolver().insert(RapidSmsDBConstants.Monitor.CONTENT_URI, initialValues);

		Cursor cr = getContext().getContentResolver().query(currUri, null, null, null, null);
		assertEquals(1, cr.getCount());
		cr.close();
	}

	public void test002MonitorInsertRepeat() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDBConstants.Monitor.PHONE, "6176453236");
		currUri = getContext().getContentResolver().insert(RapidSmsDBConstants.Monitor.CONTENT_URI, initialValues);

		Uri repeatUri = getContext().getContentResolver()
									.insert(RapidSmsDBConstants.Monitor.CONTENT_URI, initialValues);

		assertEquals(repeatUri, currUri);

	}

	public void test002MonitorInsertAndVerifyCounts() {
		int count = 1;
		int baseline = 0;
		// Log.w("ContentProviderTests.testMonitorInsertAndVerifyCounts",
		// "flasjdfklasdjf");
		Uri monitorquery = Uri.parse("content://" + RapidSmsDBConstants.AUTHORITY + "/mMonitorString");
		Cursor cr = getContext().getContentResolver().query(monitorquery, null, null, null, null);
		baseline = cr.getCount();
		cr.close();

		for (int i = 0; i < count; i++) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(RapidSmsDBConstants.Monitor.PHONE, "8887" + i);
			getContext().getContentResolver().insert(RapidSmsDBConstants.Monitor.CONTENT_URI, initialValues);
		}

		Cursor cr2 = getContext().getContentResolver().query(monitorquery, null, null, null, null);
		assertEquals(baseline + count, cr2.getCount());
		cr2.close();

	}

	String too_long = "Alert in golaoda werda no of otps only 1,other new 5 otps to start in plan.Problem in thise werda shortage of f100and75,traind manpowe,tranport do to thisez program for 1month stop but now already start it.";

	// add a bunch and confirm that the number of messages are ok
	// add messages as well and see if the number of
	public void testMessageInsertMessage() {

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

		// confirm the numbers
		Cursor msgcount = getContext().getContentResolver().query(RapidSmsDBConstants.Message.CONTENT_URI, null, null,
																	null, null);
		assertEquals(7, msgcount.getCount());
		msgcount.close();

		Cursor monitorcount = getContext().getContentResolver().query(RapidSmsDBConstants.Monitor.CONTENT_URI, null,
																		null, null, null);
		assertEquals(3, monitorcount.getCount());
		monitorcount.close();
	}

	private void doSendMessage(String msg, String date, String phone) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDBConstants.Message.MESSAGE, msg);
		initialValues.put(RapidSmsDBConstants.Message.PHONE, phone);
		initialValues.put(RapidSmsDBConstants.Message.TIME, date);
		initialValues.put(RapidSmsDBConstants.Message.IS_OUTGOING, false);
		currUri = getContext().getContentResolver().insert(RapidSmsDBConstants.Message.CONTENT_URI, initialValues);
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

		// 251912149840 = 1
		// 251913086827 = 4
		// 251911877430 = 2
		// total should be 7

		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251912149840"), 1);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251913086827"), 4);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251911877430"), 2);

		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251912149840"));
		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251913086827"));
		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251911877430"));

		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251912149840"), 0);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251913086827"), 0);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251911877430"), 0);
	}

	private int getMonitorIdForPhoneNumber(String phonenumber) {
		Cursor monitor = getContext().getContentResolver().query(RapidSmsDBConstants.Monitor.CONTENT_URI, null,
																	"phone='" + phonenumber + "'", null, null);
		monitor.moveToFirst();
		assertEquals(1, monitor.getCount());
		int ret = monitor.getInt(0);
		monitor.close();
		return ret;
	}

	private void confirmMessageCountForMonitor(int monitor_id, int expected) {
		Uri getMessagesBySingleMonitor = ContentUris.withAppendedId(RapidSmsDBConstants.Monitor.MESSAGE_BY_MONITOR_URI,
																	monitor_id);
		Cursor msgcount = getContext().getContentResolver().query(getMessagesBySingleMonitor, null, null, null, null);
		assertEquals(expected, msgcount.getCount());
		msgcount.close();
	}

	private void deleteMessagesByMonitor(int monitor_id) {
		Cursor msgcount = getContext().getContentResolver().query(RapidSmsDBConstants.Message.CONTENT_URI, null, null,
																	null, null);
		int oldcount = msgcount.getCount();
		msgcount.close();

		Uri deleteMessagesBySingleMonitor = ContentUris
														.withAppendedId(
																		RapidSmsDBConstants.Monitor.MESSAGE_BY_MONITOR_URI,
																		monitor_id);
		int delcount = getContext().getContentResolver().delete(deleteMessagesBySingleMonitor, null, null);

		Cursor newcountc = getContext().getContentResolver().query(RapidSmsDBConstants.Message.CONTENT_URI, null, null,
																	null, null);
		int newcount = newcountc.getCount();
		newcountc.close();

		assertEquals(newcount + delcount, oldcount);
		// System.out.println("deleteMessagesByMonitor: " + monitor_id +
		// " oldcount: " + oldcount + " delcount: " + delcount + " newcount: " +
		// newcount);
	}

	public void testGetFieldTypes() {
		Uri query = RapidSmsDBConstants.FieldType.CONTENT_URI;
		Cursor cr = getContext().getContentResolver().query(query, null, null, null, null);
		cr.close();
	}

	public void testGetFields() {
		Uri query = RapidSmsDBConstants.Field.CONTENT_URI;
		Cursor cr = getContext().getContentResolver().query(query, null, null, null, null);
		cr.close();
	}

	private void testGetFormData() {
		// objective:
		// get all the forms from the database
		Uri query = RapidSmsDBConstants.Form.CONTENT_URI;
		Cursor cr = getContext().getContentResolver().query(query, null, null, null, null);
		if (cr.getCount() > 0) {
			cr.moveToFirst();
			do {
				// using all the ids from the form
				// do queries off the tables in the database
				int formId = cr.getInt(0); // get the id
				Uri formDataUri = Uri.parse(RapidSmsDBConstants.FormData.CONTENT_URI_PREFIX + formId);
				Cursor formDataCursor = getContext().getContentResolver().query(formDataUri, null, null, null, null);

				formDataCursor.close();
			} while (cr.moveToNext() && cr.getCount() > 0);
		}
		cr.close();
	}

}

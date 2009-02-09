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
package org.rapidandroid.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.rapidandroid.ModelBootstrap;
import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.content.translation.ParsedDataTranslator;
import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.model.Monitor;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.service.ParsingService;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.gsm.SmsManager;
import android.test.AndroidTestCase;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Feb 6, 2009 Summary:
 */
public class MessageCorpusTests extends AndroidTestCase {

	private String[] prefixes = null;
	private Form[] forms = null;
	private Context mContext = null;

	private SmsDbHelper mHelper;

	private void initLists() {
		forms = ModelTranslator.getAllForms();
		prefixes = new String[forms.length];
		for (int i = 0; i < forms.length; i++) {
			prefixes[i] = forms[i].getPrefix();
			getContext().getContentResolver().delete(
														Uri.parse(RapidSmsDBConstants.FormData.CONTENT_URI_PREFIX
																+ forms[i].getFormId()), null, null);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.AndroidTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		ModelBootstrap.InitApplicationDatabase(getContext());
		mHelper = new SmsDbHelper(getContext());
		ModelTranslator.setDbHelper(mHelper, getContext());

		initLists();
		getContext().getContentResolver().delete(RapidSmsDBConstants.Message.CONTENT_URI, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@SuppressWarnings("finally")
	private Vector<String[]> readRawMessages() {
		String rawMessageText = loadAssetFile("testdata/rawdata.csv");
		return readCsv(rawMessageText);
	}

	/**
	 * @param rawMessageText
	 * @return
	 */
	private Vector<String[]> readCsv(String rawMessageText) {
		StringReader sr = new StringReader(rawMessageText);
		BufferedReader bufRdr = new BufferedReader(sr);

		String line = null;
		int row = 0;
		int col = 0;
		Vector<String[]> lines = new Vector<String[]>();
		// read each line of text file
		try {
			while ((line = bufRdr.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				int tokCount = st.countTokens();

				String[] tokenizedLine = new String[tokCount];
				int toki = 0;
				while (st.hasMoreTokens()) {
					tokenizedLine[toki] = st.nextToken();
					toki++;
				}
				lines.add(tokenizedLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				sr.close();
				bufRdr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lines;
	}

	private Vector<String[]> readRawAnswers() {
		String rawMessageText = loadAssetFile("testdata/answers.csv");
		return readCsv(rawMessageText);
	}

	private Form determineForm(String message) {
		int len = prefixes.length;
		for (int i = 0; i < len; i++) {
			String prefix = prefixes[i];
			if (message.toLowerCase().trim().startsWith(prefix + " ")) {
				return forms[i];
			}
		}
		return null;
	}

	/**
	 * Retains the dates in a given message
	 * 
	 * @param phone
	 * @param date
	 * @param text
	 */
	private void injectMessageDirect(String phone, Date date, String text) {
		Uri writeMessageUri = RapidSmsDBConstants.Message.CONTENT_URI;
		Monitor monitor = MessageTranslator.GetMonitorAndInsertIfNew(getContext(), phone);

		ContentValues messageValues = new ContentValues();
		messageValues.put(RapidSmsDBConstants.Message.MESSAGE, text);
		messageValues.put(RapidSmsDBConstants.Message.MONITOR, monitor.getID());

		messageValues.put(RapidSmsDBConstants.Message.TIME, Message.SQLDateFormatter.format(date));
		messageValues.put(RapidSmsDBConstants.Message.RECEIVE_TIME, Message.SQLDateFormatter.format(date));
		messageValues.put(RapidSmsDBConstants.Message.IS_OUTGOING, false);

		Uri msgUri = null;
		Form mForm = determineForm(text);
		msgUri = getContext().getContentResolver().insert(writeMessageUri, messageValues);
		Vector<IParseResult> results = ParsingService.ParseMessage(mForm, text);
		ParsedDataTranslator.InsertFormData(getContext(), mForm, Integer.valueOf(msgUri.getPathSegments().get(1))
																		.intValue(), results);
	}

	private String loadAssetFile(String filename) {
		try {
			InputStream is = getContext().getAssets().open(filename);

			int size = is.available();

			// Read the entire asset into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			// Convert the buffer into a Java string.
			String text = new String(buffer);

			return text;

		} catch (IOException e) {
			// Should never happen!
			throw new RuntimeException(e);
		}
	}

	private void sendMessageViaSMS(Context context, String mesg) {
		SmsManager smgr = SmsManager.getDefault();
		Intent intent = new Intent();
		smgr.sendTextMessage("5554", null, mesg, null, null);
	}

	public void testInsertDirect() {
		Vector<String[]> rawMessages = readRawMessages();
		int len = rawMessages.size();

		for (int i = 0; i < len; i++) {
			String[] line = rawMessages.get(i);
			assertEquals(3, line.length);

			String datestr = line[0];

			Date dateval = new Date();
			try {
				dateval = Message.SQLDateFormatter.parse(datestr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertTrue(false);
			}
			String sender = line[1];
			String text = line[2];

			injectMessageDirect(sender, dateval, text);
		}

		Cursor cr = getContext().getContentResolver().query(RapidSmsDBConstants.Message.CONTENT_URI, null, null, null,
															null);
		assertEquals(cr.getCount(), rawMessages.size());
	}

	// public void testInsertViaSendSMS() {
	// Vector<String[]> rawMessages = readRawMessages();
	// int len = rawMessages.size();
	//		
	// for(int i = 0; i < len; i++) {
	// String[] line = rawMessages.get(i);
	// assertEquals(3,line.length);
	// String text = line[2];
	//			
	// sendMessageViaSMS(getContext(),text);
	// }
	//		
	// Cursor cr =
	// getContext().getContentResolver().query(RapidSmsDBConstants.Message.CONTENT_URI,
	// null,null,null,null);
	// assertEquals(cr.getCount(),rawMessages.size());
	// }

	public void testInsertVerifyCounts() {
		testInsertDirect();
		Vector<String[]> rawMessages = readRawMessages();
		Vector<String[]> rawAnswers = readRawAnswers();
		Form mForm = null;
		assertEquals(rawMessages.size(), rawAnswers.size());
		int len = rawMessages.size();
		for (int i = 0; i < len; i++) {
			String[] line = rawMessages.get(i);
			// String[] answers = rawAnswers.get(i);
			String text = line[2];
			mForm = determineForm(text);
			if (i == 0) {
				Cursor cr = getContext().getContentResolver()
										.query(
												Uri.parse(RapidSmsDBConstants.FormData.CONTENT_URI_PREFIX
														+ mForm.getFormId()), null, null, null, null);
				assertEquals(cr.getCount(), len);
				cr.close();
				// sorry, ridiculous, i know
				break;
			}
		}

		// now, let's parse the data itself
		Cursor answercursor = getContext().getContentResolver()
											.query(
													Uri.parse(RapidSmsDBConstants.FormData.CONTENT_URI_PREFIX
															+ mForm.getFormId()), null, null, null, null);

		answercursor.moveToFirst();

		for (int i = 0; i < len; i++) {
			String[] answers = rawAnswers.get((len - 1) - i);
			// assertEquals(answercursor.getColumnCount(), 6);

			// this is cuz the answer data has a leading column
			for (int j = 1; j < answers.length; j++) {
				assertEquals(answers[j].trim().toLowerCase(), answercursor.getString(j + 1));
			}
			answercursor.moveToNext();
		}
	}

}

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

package org.rapidandroid.tests;

import java.util.Date;
import java.util.Vector;

import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.content.translation.ParsedDataTranslator;
import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.service.ParsingService;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 23, 2009 Summary:
 */
public class MessageParsingTests extends AndroidTestCase {
	String[] goodMessages = { "bednets nyc 100 30 80", "bednets lax 1500, 750 1000", "bednets lun 1214, 444 677",
			"nutrition afsdf 20 100 0.6 5 y n", "nutrition asdfwer 20kg 100cm 60% 5cm yes no",
			"nutrition asdfwqer 21 kg 100cm 60% 5cm yes no", "nutrition rqwetqwgasdfasdfweqr 2 kg 100 m 3/4 5cm yes no" };

	String[] problematic = { "bednets nyc 100 30", "bednets lax 1,500", "bednets", "bednets 100 200 300",
			"bednets 100 200 300 400", "bednets BOS 100 200 300 12321", "bednets 100 BOS 100 200 120",
			"bednets 100 BOS 100 200 120 51231", "bednetsBOS 100 200 120 51231",

			"nutrition asdfsadf 12345 20 100 0.6 5 y n", "nutrition 55446 20kg 100cm 60% 5cm yes no",
			"nutrition kh 55446 20kg 60% 5cm yes no", "nutrition 213 55446 21 100cm 60% 5cm yes no",
			"nutritions c 55446 2 kg 100 m 5cm yes no"

	};

	String[] badMessages = { "bednwafasd asd2 12983 klasd12 ajds", "nutritiasndfqwer asd2 12983 klasd12 ajds",
			"aklsjdfl234", "bedntestgklajsdljwler", "nutritionaslkdfklwer" };

	public void testGoodMessages() {

		Form[] forms = ModelTranslator.getAllForms();

		for (int q = 0; q < forms.length; q++) {

			Form form = forms[q];
			Log.d("testFactoryAndTypes", "\n\n\n******* Parsing for form : " + q + "/" + forms.length
					+ "**************");

			Log.d("testFactoryAndTypes", "Prefix: " + form.getPrefix());
			String[] messages = problematic;
			for (int i = 0; i < messages.length; i++) {
				Log.d("testFactoryAndTypes", "\tMessage " + i + " ## " + messages[i] + " ##");
				Vector<IParseResult> results = ParsingService.ParseMessage(form, messages[i]);
				if (results == null) {
					Log.d("TestFactoryAndTypes", "\tNULL Parse, invalid message");
					continue;
				}
				for (int r = 0; r < results.size(); r++) {
					Log.d("testFactoryAndTypes", "\t\t***** Parsed: " + form.getFields()[r].getName());
					if (results.get(r) == null) {
						Log.d("testFactoryAndTypes", "\t\tNULL");
					} else {
						Log.d("testFactoryAndTypes", "\t\tToken: " + results.get(r).getParsedToken());
						Log.d("testFactoryAndTypes", "\t\tSource: " + results.get(r).getSource());
						Log.d("testFactoryAndTypes", "\t\tValue: " + results.get(r).getValue());
						Log.d("testFactoryAndTypes", "\n\n");
					}
				}
			}
		}
	}

	public void testWriteMessagesToDB() {

		Form[] forms = ModelTranslator.getAllForms();

		int allcount = 0;
		for (int q = 0; q < forms.length; q++) {

			Form form = forms[q];
			ModelTranslator.generateFormTable(form);
			Log.d("testFactoryAndTypes", "\n\n\n******* Parsing for form : " + q + "/" + forms.length
					+ "**************");

			Log.d("testFactoryAndTypes", "Prefix: " + form.getPrefix());
			String[] messages = goodMessages;
			int msgcount = 0;
			for (int i = 0; i < messages.length; i++) {
				ContentValues initialValues = new ContentValues();
				initialValues.put(RapidSmsDBConstants.Message.MESSAGE, messages[i]);
				initialValues.put(RapidSmsDBConstants.Message.PHONE, "617645323" + i);
				Date dt = new Date();
				initialValues.put(RapidSmsDBConstants.Message.TIME, dt.getTime());

				initialValues.put(RapidSmsDBConstants.Message.IS_OUTGOING, false);
				Uri msgUri = getContext().getContentResolver().insert(RapidSmsDBConstants.Message.CONTENT_URI,
																		initialValues);

				Log.d("testFactoryAndTypes", "\tMessage " + i + " ## " + messages[i] + " ##");
				Vector<IParseResult> results = ParsingService.ParseMessage(form, messages[i]);
				if (results == null) {
					Log.d("TestFactoryAndTypes", "\tNULL Parse, invalid message");
					continue;
				}
				ParsedDataTranslator.InsertFormData(getContext(), form, Integer
																				.valueOf(msgUri.getPathSegments()
																								.get(1)), results);
				msgcount++;
				Cursor crinsert = getContext().getContentResolver()
												.query(
														Uri.parse(RapidSmsDBConstants.FormData.CONTENT_URI_PREFIX
																+ form.getFormId()), null, null, null, null);
				assertEquals(msgcount, crinsert.getCount());

				// assertEquals(crinsert.getColumnCount(),results.size()+1);
			}
		}
	}
}

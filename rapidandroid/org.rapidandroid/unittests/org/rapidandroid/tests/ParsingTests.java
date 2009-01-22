package org.rapidandroid.tests;

import java.util.Vector;

import org.rapidandroid.content.wrapper.ModelWrapper;
import org.rapidandroid.data.RapidSmsDataDefs;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.ParsingService;


import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;


public class ParsingTests extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	String[] goodMessages = {
			"bednets nyc 100 30 80",
			"bednets lax 1,500, 750 1000",
			"nutrition 12345 20 100 0.6 5 y n",
			"nutrition 55446 20kg 100cm 60% 5cm yes no",
			"nutrition 55446 21 kg 100cm 60% 5cm yes no",
			"nutrition 55446 2 kg 100 m 3/4 5cm yes no"			
	};
	
	String[] problematic = {
		"bednets nyc 100 30",
		"bednets lax 1,500",
		"bednets",
		"bednets 100 200 300",
		"bednets 100 200 300 400",
		"bednets BOS 100 200 300 12321",
		"bednets 100 BOS 100 200 120",
		"bednets 100 BOS 100 200 120 51231",
		"bednetsBOS 100 200 120 51231",
		
		"nutrition asdfsadf 12345 20 100 0.6 5 y n",
		"nutrition 55446 20kg 100cm 60% 5cm yes no",
		"nutrition 55446 20kg 60% 5cm yes no",
		"nutrition 55446 21 100cm 60% 5cm yes no",
		"nutrition 55446 2 kg 100 m 5cm yes no"			
	
	};
	
	String[] badMessages = {
			"bednwafasd asd2 12983 klasd12 ajds",
			"nutritiasndfqwer asd2 12983 klasd12 ajds",
			"aklsjdfl234",
			"bedntestgklajsdljwler",
			"nutritionaslkdfklwer"
	};
	
	
	public void testParsingGoodMessages() {
		
		Cursor cr = this.getContext().getContentResolver().query(RapidSmsDataDefs.Form.CONTENT_URI, null,null,null,null);
		cr.moveToFirst();
		cr.getColumnCount();
		cr.getColumnNames();
		Form[] forms = ModelWrapper.getAllForms(this.getContext());
		Log.d("ParsingTests","testParsingGoodMessages");
		for(int i = 0; i < forms.length; i++) {			
			Log.d("ParsingTests","******Form " + forms[i].getFormId());
			for(int j = 0; j < goodMessages.length; j++) {
				Log.d("ParsingTests","*********** Message: " + j);
				Vector<IParseResult> results = ParsingService.ParseMessage(forms[i],goodMessages[j]);
				for(int q = 0; q < results.size(); q++) {
					Log.d("ParsingTests", results.get(q).getSource()+ " :: " + results.get(q).getValue().toString());
				}
			}				
			
			
		}
		
		
		assertEquals(2,forms.length);
	
	}
}

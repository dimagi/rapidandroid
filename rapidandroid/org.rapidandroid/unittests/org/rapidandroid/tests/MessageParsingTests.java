package org.rapidandroid.tests;

import java.util.Vector;

import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.service.ParsingService;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 23, 2009
 * Summary:
 */
public class MessageParsingTests extends AndroidTestCase {
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
	
	public void testGoodMessages() {
		
		Form[] forms = ModelTranslator.getAllForms(getContext());

		for (int q = 0; q < forms.length; q++) {
			
			Form form = forms[q];
			Log.d("testFactoryAndTypes", "\n\n\n******* Parsing for form : " + q + "/" + forms.length + "**************");
			
			Log.d("testFactoryAndTypes", "Prefix: " + form.getPrefix());
			for (int i = 0; i < goodMessages.length; i++) {
				Log.d("testFactoryAndTypes", "\tMessage " + i + " ## " + goodMessages[i] + " ##");
				Vector<IParseResult> results = ParsingService.ParseMessage(form, goodMessages[i]);
				if(results == null) {
					Log.d("TestFactoryAndTypes", "\tNULL Parse, invalid message");
					continue;
				}
				for(int r = 0; r < results.size(); r++) {
					Log.d("testFactoryAndTypes", "\t\t***** Parsed: " + form.getFields()[r].getName());
					if(results.get(r) == null) {
						Log.d("testFactoryAndTypes", "\t\tNULL");
					} else {						
						Log.d("testFactoryAndTypes", "\t\tToken: "
								+ results.get(r).getParsedToken());
						Log.d("testFactoryAndTypes", "\t\tSource: "
								+ results.get(r).getSource());
						Log.d("testFactoryAndTypes", "\t\tValue: "
								+ results.get(r).getValue());
						Log.d("testFactoryAndTypes","\n\n");
					}
				}				
			}
		}
	}
}

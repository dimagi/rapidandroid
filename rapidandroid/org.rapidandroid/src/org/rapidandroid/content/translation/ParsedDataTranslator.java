package org.rapidandroid.content.translation;

import java.util.Random;
import java.util.Vector;

import org.rapidandroid.data.RapidSmsDataDefs;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.IParseResult;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * 
 *          Helper class to simplify the insertion and querying of parsed form
 *          data from the content provider
 */


public class ParsedDataTranslator {

	public static boolean InsertFormData(Context context, Form f, int message_id, Vector<IParseResult> results) {
		
		ContentValues cv = new ContentValues();
		cv.put(RapidSmsDataDefs.FormData.MESSAGE, message_id);
		Field[] fields = f.getFields();
		int len = fields.length;
		Random r = new Random();
		
		for(int i = 0; i < len; i++) {
			Field field = fields[i];
			IParseResult res = results.get(i);
			if(res != null) {
				cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),res.getValue().toString());
			} else {
				cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),"");
			}								
		}	
		Uri inserted = context.getContentResolver().insert(Uri.parse(RapidSmsDataDefs.FormData.CONTENT_URI_PREFIX + f.getFormId()), cv);
		return false;
	}
	
	
	
}

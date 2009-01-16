/**
 * 
 */
package org.rapidandroid.data;

import org.rapidsms.java.core.model.Form;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author dmyung
 * @created Jan 16, 2009
 */
public class ParsedFormHandler {
	
	
	public ParsedFormHandler(SQLiteOpenHelper helper) {
		//do the magic here
	}
	
	public boolean CreateTableForForm(Form form) {
		//if does exist, blow away?
		//if doesn't exist, create		
		return true;
	}

}

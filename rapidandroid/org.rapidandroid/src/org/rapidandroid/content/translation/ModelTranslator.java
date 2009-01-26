package org.rapidandroid.content.translation;

import java.util.HashMap;

import org.rapidandroid.data.RapidSmsDataDefs;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.SimpleFieldType;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.service.ParsingService.ParserType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * 
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 19, 2009
 * 
 *          Static methods to convert that wraps the ContentProvider calls
 *          and returns the actual coremodel objects.
 */

public class ModelTranslator {

	private static HashMap<String, Integer> formColumnNamesToIndex;
	private static HashMap<String, Integer> fieldColumnNamesToIndex;
	private static HashMap<String, Integer> typeColumnNamesToIndex;

	private static HashMap<Integer, SimpleFieldType> fieldTypeCache = new HashMap<Integer, SimpleFieldType>();
	private static HashMap<Uri, Form> formCache = new HashMap<Uri, Form>();;
	private static HashMap<Integer, Form> formIdCache = new HashMap<Integer, Form>();
	
	private static SmsDbHelper mDbHelper;

	
	public static void setDbHelper(SmsDbHelper helper) {
		mDbHelper = helper;
		
	}	
	
	// dmyung 1/19/2009
	// this method will have the eventual parse results populate a contentvalues
	// object for eventual insertion of a parsed message into the DB for a given
	// message for a form.
	public static void SetContentValues(ContentValues cv, IParseResult results) {
		throw new IllegalArgumentException("not implemented yet");
	}

	//public static Form[] getAllForms(ContentProvider provider) {	//hack way
	public static Form[] getAllForms(Context context) {	//real way
		Uri getFormsUri = RapidSmsDataDefs.Form.CONTENT_URI;
		
		Cursor allformsCursor = context.getContentResolver().query(getFormsUri,null,null,null,null); //real way
		//Cursor allformsCursor = provider.query(getFormsUri,null,null,null,null);	//hack way
		
		if (formColumnNamesToIndex == null) {
			formColumnNamesToIndex = new HashMap<String, Integer>();
			String[] colnames = allformsCursor.getColumnNames();
			int colcount = colnames.length;
			for (int i = 0; i < colcount; i++) {
				formColumnNamesToIndex.put(colnames[i], new Integer(allformsCursor
						.getColumnIndex(colnames[i])));
			}
		}
		int formcount = allformsCursor.getCount();
		
		Form[] ret = new Form[formcount];
		allformsCursor.moveToFirst();
		for(int i = 0; i < formcount; i++) {
			
			int id = allformsCursor.getInt(formColumnNamesToIndex.get(
					RapidSmsDataDefs.Form._ID).intValue());
			Integer idInt = Integer.valueOf(id);
			
			if (formIdCache.containsKey(idInt)) {
				ret[i] = formIdCache.get(idInt);
			}
			
			
			String name = allformsCursor.getString(formColumnNamesToIndex.get(
					RapidSmsDataDefs.Form.FORMNAME).intValue());
			String prefix = allformsCursor.getString(formColumnNamesToIndex.get(
					RapidSmsDataDefs.Form.PREFIX).intValue());
			String description = allformsCursor.getString(formColumnNamesToIndex.get(
					RapidSmsDataDefs.Form.DESCRIPTION).intValue());
			String parsemethod = allformsCursor.getString(formColumnNamesToIndex.get(
					RapidSmsDataDefs.Form.PARSEMETHOD).intValue());

			//Field[] fields = getFieldsForForm(provider, id); // hack way
			Field[] fields = getFieldsForForm(context, id); //real way

			Form theForm = new Form(id, name, prefix, description, fields,ParserType.SIMPLEREGEX);

			formIdCache.put(idInt, theForm);
			ret[i] = theForm; 
			allformsCursor.moveToNext();
		}		
		allformsCursor.close();
		return ret;		
	}
	
	// dmyung 1/19/2009
	// NOTE, passing the ContextProvider is a seriously BAD hack right now to
	// get the ContentProvider tests
	// accessing this to pass.
	// in reality you should only be able to pass the context and must use the
	// GetContentResolver() to access any resources via URIs!!!!

	public static Form getFormFromUri(Context context, Uri formUri) {
	//public static Form getFormFromUri(ContentProvider provider, Uri formUri) {

		if (formCache.containsKey(formUri)) {
			return formCache.get(formUri);
		}

		//Cursor formCursor = provider.query(formUri, null, null, null, null); // hack
																				// way
		
		Cursor formCursor = context.getContentResolver().query(formUri,null,null,null,null); //real way
		if (formCursor.getCount() != 1) {
			throw new IllegalArgumentException(formUri
					+ " returned a bad result.");
		}

		if (formColumnNamesToIndex == null) {
			formColumnNamesToIndex = new HashMap<String, Integer>();
			String[] colnames = formCursor.getColumnNames();
			int colcount = colnames.length;
			for (int i = 0; i < colcount; i++) {
				formColumnNamesToIndex.put(colnames[i], new Integer(formCursor
						.getColumnIndex(colnames[i])));
			}
		}

		formCursor.moveToFirst();
		int id = formCursor.getInt(formColumnNamesToIndex.get(
				RapidSmsDataDefs.Form._ID).intValue());
		String name = formCursor.getString(formColumnNamesToIndex.get(
				RapidSmsDataDefs.Form.FORMNAME).intValue());
		String prefix = formCursor.getString(formColumnNamesToIndex.get(
				RapidSmsDataDefs.Form.PREFIX).intValue());
		String description = formCursor.getString(formColumnNamesToIndex.get(
				RapidSmsDataDefs.Form.DESCRIPTION).intValue());
		String parsemethod = formCursor.getString(formColumnNamesToIndex.get(
				RapidSmsDataDefs.Form.PARSEMETHOD).intValue());

		//Field[] fields = getFieldsForForm(provider, id); // hack way
		Field[] fields = getFieldsForForm(context, id); //real way

		Form ret = new Form(formCursor.getInt(0), name, prefix, description, fields,ParserType.SIMPLEREGEX);
		formCache.put(formUri, ret);
		formCursor.close();
		return ret;
	}

	 public static Field[] getFieldsForForm(Context context, int formId) {
	// //real way
	//public static Field[] getFieldsForForm(ContentProvider provider, int formId) { // hack
																					// way

		Context c;
		
		Uri fieldsUri = RapidSmsDataDefs.Field.CONTENT_URI;
		 Cursor fieldsCursor = context.getContentResolver().query(fieldsUri,
		 null, RapidSmsDataDefs.Field.FORM + "=" + formId,
		 null,"sequence ASC"); //real way

		 //		Cursor fieldsCursor = provider.query(fieldsUri, null,
//				RapidSmsDataDefs.Field.FORM + "=" + formId, null,
//				"sequence ASC");// hack way

		if (fieldColumnNamesToIndex == null) {
			fieldColumnNamesToIndex = new HashMap<String, Integer>();
			String[] colnames = fieldsCursor.getColumnNames();
			int colcount = colnames.length;
			for (int i = 0; i < colcount; i++) {
				fieldColumnNamesToIndex.put(colnames[i], new Integer(
						fieldsCursor.getColumnIndex(colnames[i])));
			}
		}
		Field[] newfields = new Field[fieldsCursor.getCount()];
		fieldsCursor.moveToFirst();
		int fieldcount = 0;
		do {
			int id = fieldsCursor.getInt(fieldColumnNamesToIndex.get(
					RapidSmsDataDefs.Field._ID).intValue());
			String name = fieldsCursor.getString(fieldColumnNamesToIndex.get(
					RapidSmsDataDefs.Field.NAME).intValue());
			String prompt = fieldsCursor.getString(fieldColumnNamesToIndex.get(
					RapidSmsDataDefs.Field.PROMPT).intValue());
			int sequence = fieldsCursor.getInt(fieldColumnNamesToIndex.get(
					RapidSmsDataDefs.Field.SEQUENCE).intValue());
			int fieldtype = fieldsCursor.getInt(fieldColumnNamesToIndex.get(
					RapidSmsDataDefs.Field.FIELDTYPE).intValue());

			 Field newField = new Field(id,sequence,name,prompt,getFieldType(context,fieldtype));
			// //real way
			//Field newField = new Field(id, sequence, name, prompt,getFieldType(provider, fieldtype));// hack way
			newfields[fieldcount++] = newField;

		} while (fieldsCursor.moveToNext());

		fieldsCursor.close();
		return newfields;
	}

	 public static SimpleFieldType getFieldType(Context context, int type_id) {
	// //real way
	//public static SimpleFieldType getFieldType(ContentProvider provider, int type_id) { // hack
																					// way

		Integer typeInt = new Integer(type_id);
		if (fieldTypeCache.containsKey(typeInt)) {
			return fieldTypeCache.get(typeInt);
		}
		Uri typeUri = Uri.parse(RapidSmsDataDefs.FieldType.CONTENT_URI_STRING
				+ type_id);
		 Cursor typeCursor = context.getContentResolver().query(typeUri,null,null, null, null); //real way
		//Cursor typeCursor = provider.query(typeUri, null, null, null, null); // hack
																				// way

		if (typeColumnNamesToIndex == null) {
			typeColumnNamesToIndex = new HashMap<String, Integer>();
			String[] colnames = typeCursor.getColumnNames();
			int colcount = colnames.length;
			for (int i = 0; i < colcount; i++) {
				typeColumnNamesToIndex.put(colnames[i], new Integer(typeCursor
						.getColumnIndex(colnames[i])));
			}
		}
		if (typeCursor.getCount() != 1) {
			throw new IllegalArgumentException(typeUri
					+ " returned a bad result.");
		}

		typeCursor.moveToFirst();

		int id = typeCursor.getInt(typeColumnNamesToIndex.get(
				RapidSmsDataDefs.FieldType._ID).intValue());
		String dataType = typeCursor.getString(typeColumnNamesToIndex.get(
				RapidSmsDataDefs.FieldType.DATATYPE).intValue());
		String name = typeCursor.getString(typeColumnNamesToIndex.get(
				RapidSmsDataDefs.FieldType.NAME).intValue());
		String regex = typeCursor.getString(typeColumnNamesToIndex.get(
				RapidSmsDataDefs.FieldType.REGEX).intValue());

		// SimpleFieldType ftype) {
		SimpleFieldType newType = new SimpleFieldType(id, dataType, regex, name);
		fieldTypeCache.put(typeInt, newType);
		typeCursor.close();
		return newType;

	}
	
	private static void getFieldDeclaration(Field field, StringBuilder sb, boolean last) {

		sb.append(" \"");
		sb.append("col_" + field.getName());
		sb.append("\"");
		if (field.getFieldType().getItemType().equals("integer")) {
			sb.append(" integer NULL");
		} else if (field.getFieldType().getItemType().equals("number")) {
			sb.append(" integer NULL");
		} else if (field.getFieldType().getItemType().equals("boolean")) {
			sb.append(" bool NULL");
		} else if (field.getFieldType().getItemType().equals("word")) {
			sb.append(" varchar(36) NULL");
		} else if (field.getFieldType().getItemType().equals("ratio")) {
			sb.append(" varchar(36) NULL");
		} else if (field.getFieldType().getItemType().equals("datetime")) {
			sb.append(" datetime NULL");
		}
		if (!last) {
			sb.append(", ");
		}
	}

	public static void ClearFormTables() {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		db.execSQL("delete from " + RapidSmsDataDefs.FieldType.TABLE);
		db.execSQL("delete from " + RapidSmsDataDefs.Field.TABLE);
		db.execSQL("delete from " + RapidSmsDataDefs.Form.TABLE);

		Log.v("dimagi", "wiped the form/field/fieldtype/formdata table for debug purposes");
	}
	
	
	public static void generateFormTable(Form form) {
		// dmyung: 1/19/2009
		// For the intial run through this is a bit hacky.

		// for each form, create a new sql table create table script
		// do do that get the form prefix and get a foriegn key back to the
		// message table
		// after that, create all the columns
		// do do this we make a switch statement and we will support the SQLite
		// datatypes.
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		try {
			db.execSQL("drop table formdata_" + form.getFormId());
		} catch(SQLException ex) {
			//ok the table didn't exist
		}

		StringBuilder sb = new StringBuilder();
		sb.append("create table formdata_");
		sb.append(form.getFormId());
		sb.append(" (");
		sb.append(" \"_id\" integer not null PRIMARY KEY, ");
		sb.append(" \"message_id\" integer not null references \"message\", ");

		org.rapidsms.java.core.model.Field[] fields = form.getFields();
		int fieldcount = fields.length;

		boolean last = false;
		for (int i = 0; i < fieldcount; i++) {
			if (i == fieldcount - 1) {
				last = true;
			}
			getFieldDeclaration(fields[i], sb, last);
		}

		sb.append(" );");

		db.execSQL(sb.toString());
	}

}

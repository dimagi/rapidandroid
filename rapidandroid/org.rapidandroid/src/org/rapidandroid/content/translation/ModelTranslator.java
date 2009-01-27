package org.rapidandroid.content.translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

	private static Context mContext;
	
	private static HashMap<String, Integer> formColumnNamesToIndex;
	private static HashMap<String, Integer> fieldColumnNamesToIndex;
	private static HashMap<String, Integer> typeColumnNamesToIndex;
	
	private static HashMap<Integer, Form> formIdCache = new HashMap<Integer, Form>();
	private static HashMap<Integer, Vector<Field>> fieldToFormHash = new HashMap<Integer,Vector<Field>> ();
	private static HashMap<Integer, SimpleFieldType> fieldTypeHash = new HashMap<Integer, SimpleFieldType>();
	
	private static SmsDbHelper mDbHelper;

	private static String loadAssetFile(String filename){
        try {
            InputStream is = mContext.getAssets().open(filename);
                      
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
	
	/**
	 * Initial app startup, ONLY SHOULD BE RUN ONCE!!!
	 * called when the existence of some data in the fieldtypes table is missing.
	 */
	private static void applicationInitialFormFieldTypesBootstrap() {
		loadFieldTypesFromAssets();
		loadInitialFormsFromAssets();		
		insertFieldTypesIntoDBIfNecessary();
		checkIfFormTablesExistCreateIfNecessary();
	}
	
	private static void insertFieldTypesIntoDBIfNecessary() {
		
		Iterator<?> it = fieldTypeHash.entrySet().iterator();
		
		//for(int i = 0; i < forms.size(); i++) {
		while (it.hasNext()) {
			Map.Entry<Integer, SimpleFieldType> pairs = (Map.Entry<Integer, SimpleFieldType>)it.next();	
			SimpleFieldType thetype = pairs.getValue();
			//make the URI and insert for the Fieldtype			

			Uri fieldtypeUri = Uri.parse(RapidSmsDataDefs.FieldType.CONTENT_URI_STRING + thetype.getId());
			Cursor typeCursor = mContext.getContentResolver().query(fieldtypeUri, null, null, null, null);
			if (typeCursor.getCount() == 0) {
				ContentValues typecv = new ContentValues();

				typecv.put(RapidSmsDataDefs.FieldType._ID, thetype.getId());					
				typecv.put(RapidSmsDataDefs.FieldType.DATATYPE, thetype.getDataType());
				typecv.put(RapidSmsDataDefs.FieldType.NAME, thetype.getTokenName());
				typecv.put(RapidSmsDataDefs.FieldType.REGEX, thetype.getRegex());
				
				Log.d("dimagi", "InsertFieldType: " + thetype.getId());
				Log.d("dimagi", "InsertFieldType: " + thetype.getDataType());
				Log.d("dimagi", "InsertFieldType: " + thetype.getTokenName());
				Log.d("dimagi", "InsertFieldType: " + thetype.getRegex());

				Uri insertedTypeUri = mContext.getContentResolver().insert(RapidSmsDataDefs.FieldType.CONTENT_URI, typecv);
				Log.d("dimagi","********** Inserted SimpleFieldType into db: " + insertedTypeUri);										
			}
			typeCursor.close();
		}
		
		
		
		
	}
	
	private static void loadFieldTypesFromAssets(){
		String types = loadAssetFile("definitions/fieldtypes.json");
		try {
			JSONArray typesarray = new JSONArray(types);
			
			int arrlength = typesarray.length();
			for(int i = 0; i < arrlength; i++) {
				try {
					JSONObject obj = typesarray.getJSONObject(i);
					Log.d("dimagi", "type loop: " + i + " model: " + obj.getString("model"));
					if(!obj.getString("model").equals("rapidandroid.fieldtype")) {
						Log.d("dimagi", "###" + obj.getString("model")+ "###");
						throw new IllegalArgumentException("Error in parsing fieldtypes.json");
					}
					
					int pk = obj.getInt("pk");
					JSONObject jsonfields = obj.getJSONObject("fields");					
					//Log.d("dimagi", "#### Parsing SimpleFieldType: " + jsonfields.getString("name") + " [" + hackRegexHash.get(jsonfields.getString("name")) + "]");
					Log.d("dimagi", "#### Regex from file: " + jsonfields.getString("name") + " [" + jsonfields.getString("regex") + "]");
					//SimpleFieldType newtype = new SimpleFieldType(pk, jsonfields.getString("datatype"),hackRegexHash.get(jsonfields.getString("name")),jsonfields.getString("name"));
					SimpleFieldType newtype = new SimpleFieldType(pk, jsonfields.getString("datatype"),jsonfields.getString("regex"),jsonfields.getString("name"));
					fieldTypeHash.put(new Integer(pk), newtype);					
				} catch (JSONException e) {					
				}	
			}
		} catch (JSONException e) {			
		}
	}
	
	private static void loadInitialFormsFromAssets(){
		parseFields();
		parseForms();
		
	}
	
	private static void parseFields() {
		String fields = loadAssetFile("definitions/fields.json");
		try {
			JSONArray fieldsarray = new JSONArray(fields);
			int arrlength = fieldsarray.length();
			for(int i = 0; i < arrlength; i++) {
				try {
					JSONObject obj = fieldsarray.getJSONObject(i);
					
					if(!obj.getString("model").equals("rapidandroid.field")) {
						
					}
					
					int pk = obj.getInt("pk");
					
					
					JSONObject jsonfields = obj.getJSONObject("fields");
					int form_id = jsonfields.getInt("form");
					//public Field(int id, int sequence, String name, String prompt, SimpleFieldType ftype) {
					Field newfield = new Field(pk, 
							jsonfields.getInt("sequence"),
							jsonfields.getString("name"), 
							jsonfields.getString("prompt"),
							fieldTypeHash.get(new Integer(jsonfields.getInt("fieldtype"))));
					
					//fieldHash.put(new Integer(pk), newfield);
					Integer formInt = Integer.valueOf(form_id);
					if(!fieldToFormHash.containsKey(formInt)) {
						fieldToFormHash.put(formInt,new Vector<Field>());
						Log.d("dimagi","### adding a key again?!" + formInt);
					}
					fieldToFormHash.get(formInt).add(newfield);
					Log.d("dimagi", "#### Parsed field: " + newfield.getFieldId() + " [" + newfield.getName() + "] newlength: " + fieldToFormHash.get(formInt).size());
					
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("dimagi", e.getMessage());
					
				}	
			}
		} catch (JSONException e) {						
		}
	}
	
	private static void parseForms() {
		String forms = loadAssetFile("definitions/forms.json");
		
		try {
			JSONArray formarray = new JSONArray(forms);
			int arrlength = formarray.length();
			for(int i = 0; i < arrlength; i++) {
				try {
					JSONObject obj = formarray.getJSONObject(i);
					
					if(!obj.getString("model").equals("rapidandroid.form")) {						
					}
					
					int pk = obj.getInt("pk");
					Integer pkInt = new Integer(pk);
					JSONObject jsonfields = obj.getJSONObject("fields");		
					
					Field[] fieldarr = new Field[fieldToFormHash.get(pkInt).size()];
					for (int q = 0; q < fieldarr.length; q++) {
						fieldarr[q] = fieldToFormHash.get(pkInt).get(q);
					}
					Form newform = new Form(pk, jsonfields.getString("formname"),
												jsonfields.getString("prefix"),
												jsonfields.getString("description"),												
												fieldarr,
												ParserType.SIMPLEREGEX);
					//allforms.add(newform);
					formIdCache.put(pkInt, newform);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("dimagi", e.getMessage());					
				}	
			}
		} catch (JSONException e) {			
		}
	}
	
	private static void checkIfFormTablesExistCreateIfNecessary() {
		//so, todo:
		//check if tables exist
		//else
		
		 Iterator<?> it = formIdCache.entrySet().iterator();
		
		//for(int i = 0; i < forms.size(); i++) {
		while (it.hasNext()) {
			Map.Entry<Integer, Form> pairs = (Map.Entry<Integer, Form>)it.next();
			Form f = pairs.getValue();
			Field[] fields = f.getFields();
			Log.d("dimagi","**** inserting form " + f.getFormName());
			
			//insert the form first
			Uri formUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + f.getFormId());
			Cursor crform = mContext.getContentResolver().query(formUri, null, null, null, null);
			if (crform.getCount() == 0) {
				ContentValues typecv = new ContentValues();

				typecv.put(RapidSmsDataDefs.Form._ID, f.getFormId());
				typecv.put(RapidSmsDataDefs.Form.FORMNAME, f.getFormName());
				typecv.put(RapidSmsDataDefs.Form.PARSEMETHOD, "simpleregex");	//eww, hacky magic string
				typecv.put(RapidSmsDataDefs.Form.PREFIX, f.getPrefix());
				typecv.put(RapidSmsDataDefs.Form.DESCRIPTION, f.getDescription());							

				Uri insertedFormUri = mContext.getContentResolver().insert(RapidSmsDataDefs.Form.CONTENT_URI, typecv);
				Log.d("dimagi","****** Inserted form into db: " + insertedFormUri);									
			}		
			crform.close();
			
			Log.d("dimagi","****** Begin fields loop: " + fields.length);
			for(int j = 0; j < fields.length; j++) {
				Field thefield = fields[j];
				Log.d("dimagi","******** Iterating through fields: " + thefield.getName() + " id: " + thefield.getFieldId());
				Uri fieldUri = Uri.parse(RapidSmsDataDefs.Field.CONTENT_URI_STRING + thefield.getFieldId());
				Cursor crfield = mContext.getContentResolver().query(fieldUri, null, null, null, null);
				if (crfield.getCount() == 0) {
					ContentValues typecv = new ContentValues();

					typecv.put(RapidSmsDataDefs.Field._ID, thefield.getFieldId());
					typecv.put(RapidSmsDataDefs.Field.NAME, thefield.getName());
					typecv.put(RapidSmsDataDefs.Field.FORM, f.getFormId());
					typecv.put(RapidSmsDataDefs.Field.PROMPT, thefield.getPrompt());
					typecv.put(RapidSmsDataDefs.Field.SEQUENCE, thefield.getSequenceId());
					
					typecv.put(RapidSmsDataDefs.Field.FIELDTYPE, ((SimpleFieldType) (thefield.getFieldType())).getId());
					//typecv.put(RapidSmsDataDefs.Field.FIELDTYPE, thefield.getFieldType().getId());	
					
//					Log.d("dimagi", "_ID: " + thefield.getFieldId());
//					Log.d("dimagi", "NAME: " + thefield.getName());
//					Log.d("dimagi", "FORM: " + f.getFormId());
//					Log.d("dimagi", "PROMPT: " + thefield.getPrompt());
//					Log.d("dimagi", "SEQUENCE: " + thefield.getSequenceId());
//					Log.d("dimagi", "FIELDTYPE: " + thefield.getFieldType().getId());

					Uri insertedFieldUri = mContext.getContentResolver().insert(RapidSmsDataDefs.Field.CONTENT_URI, typecv);
					Log.d("dimagi","********** Inserted Field into db: " + insertedFieldUri);
				}			
				crfield.close();
				//next, make the uri and insert for the field.
			}
		}
	}
	
	
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

		Integer formid = Integer.valueOf(formUri.getPathSegments().get(1));
		if (formIdCache.containsKey(formid)) {
			return formIdCache.get(formid);
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
		formIdCache.put(Integer.valueOf(id), ret);
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
		if (fieldTypeHash.containsKey(typeInt)) {
			return fieldTypeHash.get(typeInt);
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
		fieldTypeHash.put(typeInt, newType);
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

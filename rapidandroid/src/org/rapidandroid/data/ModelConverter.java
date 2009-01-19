/**
 * 
 */
package org.rapidandroid.data;

import java.util.HashMap;
import java.util.Vector;

import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.FieldType;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.ParseResult;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author dmyung
 * @created Jan 19, 2009
 */
public class ModelConverter {

	private static HashMap<String, Integer> formColumnNamesToIndex;
	private static HashMap<String, Integer> fieldColumnNamesToIndex;
	private static HashMap<String, Integer> typeColumnNamesToIndex;
	
	private static HashMap<Integer, FieldType> fieldTypeCache = new HashMap<Integer, FieldType>();
	private static HashMap<Uri,Form> formCache = new HashMap<Uri, Form>();;
	
	
	public static void SetContentValues(ContentValues cv, ParseResult results) {
		throw new IllegalArgumentException("not implemented yet");
	}
	
	//dmyung 1/19/2009
	//NOTE, passing the ContextProvider is a seriously BAD hack right now to get the ContentProvider tests
	//accessing this to pass.
	//in reality you should only be able to pass the context and must use the GetContentResolver() to access any resources via URIs!!!!

	
	//public static Form getFormFromUri(Context context, Uri formUri) {
	public static Form getFormFromUri(ContentProvider provider, Uri formUri) {
	
		
		if(formCache.containsKey(formUri)) {
			return formCache.get(formUri);
		}
		
		Cursor formCursor = provider.query(formUri, null,null,null,null); //hack way				
		//Cursor formCursor = context.getContextResolver().query(formUri, null,null,null,null);	//real way
		if(formCursor.getCount() != 1) {
			throw new IllegalArgumentException(formUri + " returned a bad result.");
		}
		
		if(formColumnNamesToIndex == null) {
			formColumnNamesToIndex = new HashMap<String,Integer>();
			String[] colnames = formCursor.getColumnNames();
			int colcount = colnames.length;
			for(int i = 0; i < colcount; i++) {
				formColumnNamesToIndex.put(colnames[i], new Integer(formCursor.getColumnIndex(colnames[i])));
			}
			
		}
		
		formCursor.moveToFirst();
		int id = formCursor.getInt(formColumnNamesToIndex.get(RapidSmsDataDefs.Form._ID).intValue());
		String name = formCursor.getString(formColumnNamesToIndex.get(RapidSmsDataDefs.Form.FORMNAME).intValue());
		String prefix = formCursor.getString(formColumnNamesToIndex.get(RapidSmsDataDefs.Form.PREFIX).intValue());
		String description = formCursor.getString(formColumnNamesToIndex.get(RapidSmsDataDefs.Form.DESCRIPTION).intValue());
		String parsemethod = formCursor.getString(formColumnNamesToIndex.get(RapidSmsDataDefs.Form.PARSEMETHOD).intValue());
		
		Field[] fields = getFieldsForForm(provider, id);	//hack way
		//Field[] fields = getFieldsForForm(context, id);	//real way
		
		Form ret = new Form(formCursor.getInt(0),name,prefix,description,parsemethod,fields);
		
		formCache.put(formUri, ret);
		return ret;
	}
	
	//public static Field[] getFieldsForForm(Context context, int formId) {	//real way
	public static Field[] getFieldsForForm(ContentProvider provider, int formId) {	//hack way
		
		
		Uri fieldsUri = RapidSmsDataDefs.Field.CONTENT_URI;
		//Cursor fieldsCursor = context.GetContentResolver().query(fieldsUri, null, RapidSmsDataDefs.Field.FORM + "=" + formId, null,"sequence ASC"); //real way
		Cursor fieldsCursor = provider.query(fieldsUri, null, RapidSmsDataDefs.Field.FORM + "=" + formId, null,"sequence ASC");//hack way
		
		if(fieldColumnNamesToIndex == null) {
			fieldColumnNamesToIndex = new HashMap<String,Integer>();
			String[] colnames = fieldsCursor.getColumnNames();
			int colcount = colnames.length;
			for(int i = 0; i < colcount; i++) {
				fieldColumnNamesToIndex.put(colnames[i], new Integer(fieldsCursor.getColumnIndex(colnames[i])));
			}			
		}		
		Field[] newfields = new Field[fieldsCursor.getCount()];
		fieldsCursor.moveToFirst();
		int fieldcount= 0;
		do {
			int id = fieldsCursor.getInt(fieldColumnNamesToIndex.get(RapidSmsDataDefs.Field._ID).intValue());
			String name = fieldsCursor.getString(fieldColumnNamesToIndex.get(RapidSmsDataDefs.Field.NAME).intValue());
			String prompt = fieldsCursor.getString(fieldColumnNamesToIndex.get(RapidSmsDataDefs.Field.PROMPT).intValue());
			int sequence = fieldsCursor.getInt(fieldColumnNamesToIndex.get(RapidSmsDataDefs.Field.SEQUENCE).intValue());
			int fieldtype = fieldsCursor.getInt(fieldColumnNamesToIndex.get(RapidSmsDataDefs.Field.FIELDTYPE).intValue());

			
			//Field newField = new Field(id,sequence,name,prompt,getFieldType(context,fieldtype));	//real way
			Field newField = new Field(id,sequence,name,prompt,getFieldType(provider,fieldtype));//hack way
			newfields[fieldcount++] = newField;
			
		} while (fieldsCursor.moveToNext());		
		
		return newfields;		
	}

	//public static FieldType getFieldType(Context context, int type_id) {	//real way
	public static FieldType getFieldType(ContentProvider provider, int type_id) {	//hack way
		
		Integer typeInt = new Integer (type_id);
		if(fieldTypeCache.containsKey(typeInt)) {
			return fieldTypeCache.get(typeInt);
		}
		Uri typeUri = Uri.parse(RapidSmsDataDefs.FieldType.CONTENT_URI_STRING
				+ type_id);
		//Cursor typeCursor = context.GetContentResolver().query(typeUri, null,null, null, null);	//real way
		Cursor typeCursor = provider.query(typeUri, null,null, null, null);	//hack way

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

		
		// FieldType ftype) {
		FieldType newType = new FieldType(id, dataType,regex,name);		
		fieldTypeCache.put(typeInt, newType);
		return newType;

	}
	
}

package org.rapidandroid;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.data.RapidSmsDataDefs;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.SimpleFieldType;
import org.rapidsms.java.core.parser.service.ParsingService.ParserType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 27, 2009
 * Summary:
 */
public class ModelBootstrap {
	
	private static Context mContext;
	
	private static HashMap<Integer, Form> formIdCache = new HashMap<Integer, Form>();
	private static HashMap<Integer, Vector<Field>> fieldToFormHash = new HashMap<Integer,Vector<Field>> ();
	private static HashMap<Integer, SimpleFieldType> fieldTypeHash = new HashMap<Integer, SimpleFieldType>();
	
	public static void InitApplicationDatabase(Context context) {
		mContext = context;
		//check existence of tables and forms
		if(isFieldTypeTableEmpty()) {
			applicationInitialFormFieldTypesBootstrap();
		}
	}
	
	private static boolean isFieldTypeTableEmpty() {
		Uri fieldtypeUri = RapidSmsDataDefs.FieldType.CONTENT_URI;
		Cursor fieldtypecheck = mContext.getContentResolver().query(fieldtypeUri, null, null, null, null);
		if(fieldtypecheck.getCount() == 0) {
			fieldtypecheck.close();
			return true;
		} else {
			//not empty!
			fieldtypecheck.close();
			return false;
		}
	}
	
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
		insertFieldTypesIntoDBIfNecessary();
		
		
		loadInitialFormsFromAssets();		
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

				typecv.put(BaseColumns._ID, thetype.getId());					
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
					Log.d("dimagi", "#### Regex from file: " + jsonfields.getString("name") + " [" + jsonfields.getString("regex") + "]");
					SimpleFieldType newtype = new SimpleFieldType(pk, jsonfields.getString("datatype"),jsonfields.getString("regex"),jsonfields.getString("name"));
					fieldTypeHash.put(new Integer(pk), newtype);					
				} catch (JSONException e) {					
				}	
			}
		} catch (JSONException e) {			
		}
	}
	
	private static void loadInitialFormsFromAssets(){
		parseFieldsFromAssets();
		parseFormsFromAssets();				
	}
	
	private static void parseFieldsFromAssets() {
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
					Field newfield = new Field(pk, 
							jsonfields.getInt("sequence"),
							jsonfields.getString("name"), 
							jsonfields.getString("prompt"),
							fieldTypeHash.get(new Integer(jsonfields.getInt("fieldtype"))));
					
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
	
	private static void parseFormsFromAssets() {
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
			
			Log.d("dimagi","**** inserting form " + f.getFormName());
			
			//insert the form first
			Uri formUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + f.getFormId());
			Cursor crform = mContext.getContentResolver().query(formUri, null, null, null, null);
			boolean newFormInserted = false;
			if (crform.getCount() == 0) {
				ModelTranslator.addFormToDatabase(f);
			} 
			crform.close();
			
		}
	}
}

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 21, 2009
 * Summary:
 */
package org.rapidandroid.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;
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
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * @author dmyung
 * @created Jan 21, 2009
 */
public class ContentBootstrapTests extends AndroidTestCase {

	protected String loadAssetFile(String filename){
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
	
	//First level bootstrap of Form definitions into DB.
	public void test000BootstrapFormsAndInsertIntoDB() {
				
		ModelTranslator.ClearFormTables();
		
		String fields =   loadAssetFile("definitions/fields.json");//"[{\"pk\": 1, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 1, \"prompt\": \"Location of distribution center\", \"name\": \"Location\", \"form\": 1, \"sequence\": 1}}, {\"pk\": 2, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 2, \"prompt\": \"Number of bednets received\", \"name\": \"received\", \"form\": 1, \"sequence\": 2}}, {\"pk\": 3, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 2, \"prompt\": \"Number of bednets that have been handed out\", \"name\": \"given\", \"form\": 1, \"sequence\": 3}}, {\"pk\": 4, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 2, \"prompt\": \"Number of bednets that remain in balance\", \"name\": \"balance\", \"form\": 1, \"sequence\": 4}}, {\"pk\": 5, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 1, \"prompt\": \"Child Identifier (6 digits)\", \"name\": \"child_id\", \"form\": 2, \"sequence\": 1}}, {\"pk\": 6, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 3, \"prompt\": \"weight\", \"name\": \"weight\", \"form\": 2, \"sequence\": 2}}, {\"pk\": 7, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 4, \"prompt\": \"height\", \"name\": \"height\", \"form\": 2, \"sequence\": 3}}, {\"pk\": 8, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 5, \"prompt\": \"ratio\", \"name\": \"ratio\", \"form\": 2, \"sequence\": 4}}, {\"pk\": 9, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 6, \"prompt\": \"muac\", \"name\": \"muac\", \"form\": 2, \"sequence\": 5}}, {\"pk\": 10, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 7, \"prompt\": \"Does child suffer from oedema\", \"name\": \"oedema\", \"form\": 2, \"sequence\": 6}}, {\"pk\": 11, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 7, \"prompt\": \"does the child suffer from diarrhoea\", \"name\": \"diarrhoea\", \"form\": 2, \"sequence\": 7}}]";
		String forms = loadAssetFile("definitions/forms.json");//"[{\"pk\": 1, \"model\": \"rapidandroid.form\", \"fields\": {\"parsemethod\": \"simpleregex\", \"prefix\": \"bednets\", \"description\": \"Bednet Distribution(supply)\", \"formname\": \"bednets\"}}, {\"pk\": 2, \"model\": \"rapidandroid.form\", \"fields\": {\"parsemethod\": \"simpleregex\", \"prefix\": \"nutrition\", \"description\": \"Nutrition Information (monitorin and evaluation)\", \"formname\": \"Nutrition\"}}]";
		String types = loadAssetFile("definitions/fieldtypes.json");//"[{\"pk\": 1, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"word\", \"regex\": \"\\w+\", \"name\": \"word\"}}, {\"pk\": 2, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"number\", \"regex\": \"\\d+\", \"name\": \"number\"}}, {\"pk\": 3, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"float\", \"regex\": \"(\\d+{1,3})(?c:\\s*(kg|kilo|kilos|lb|lbs|pounds))\", \"name\": \"weight\"}}, {\"pk\": 4, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"integer\", \"regex\": \"(\\d+{1,3})(?:\\s*(cm|m|in))\", \"name\": \"height\"}}, {\"pk\": 5, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"float\", \"regex\": \"(\\d+\\:\\d+)|(\\d+\\/\\d+)\", \"name\": \"ratio\"}}, {\"pk\": 6, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"integer\", \"regex\": \"(\\d+{1,3})(?:\\s*(cm|mm|m|in|ft|feet|meter|meters))\", \"name\": \"length\"}}, {\"pk\": 7, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"boolean\", \"regex\": \"(t|f|true|false|y|n|yes|no)\", \"name\": \"boolean\"}}]";
		
		String formdefs = "";		
		
		HashMap<Integer, SimpleFieldType> typeHash = new HashMap<Integer,SimpleFieldType>();
		HashMap<Integer, Field> fieldHash = new HashMap<Integer,Field>();
		HashMap<Integer, Form> formHash = new HashMap<Integer,Form>();
		HashMap<Integer, Vector<Field>> fieldToFormHash = new HashMap<Integer,Vector<Field>> ();
		Vector<Form> allforms = new Vector<Form>();
		
		boolean fail  = false;
		
		parseFieldTypes(types, typeHash);		
		parseFields(fields, typeHash, fieldToFormHash);		
		parseForms(forms, fieldToFormHash, allforms);
						
		assertEquals(2, allforms.size());
		Log.d("dimagi", "Test bootstrap success");
		
		insertFormsIntoDb(allforms);
	}	

	/**
	 * @param forms
	 * @param fieldToFormHash
	 * @param allforms
	 */
	private void parseForms(String forms,
			HashMap<Integer, Vector<Field>> fieldToFormHash,
			Vector<Form> allforms) {
		//ok, let's get the forms
		try {
			JSONArray formarray = new JSONArray(forms);
			int arrlength = formarray.length();
			for(int i = 0; i < arrlength; i++) {
				try {
					JSONObject obj = formarray.getJSONObject(i);
					
					if(!obj.getString("model").equals("rapidandroid.form")) {
						assertTrue(false);
					}
					
					int pk = obj.getInt("pk");
					Integer pkInt = new Integer(pk);
					JSONObject jsonfields = obj.getJSONObject("fields");		

					//public Form(int id, String name, String prefix, String desc, String parsetype, Field[] fields)
					
					
					int formcount = 0;				
					Field[] fieldarr = new Field[fieldToFormHash.get(pkInt).size()];
					for (int q = 0; q < fieldarr.length; q++) {
						fieldarr[q] = fieldToFormHash.get(pkInt).get(q);
					}
					Form newform = new Form(pk, jsonfields.getString("formname"),
												jsonfields.getString("prefix"),
												jsonfields.getString("description"),												
												fieldarr,
												ParserType.SIMPLEREGEX);
					allforms.add(newform);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("dimagi", e.getMessage());
					assertTrue(false);
				}	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("testBootstrapForms.formsouter", e.getMessage());
			assertTrue(false);
		}
	}

	/**
	 * @param fields
	 * @param typeHash
	 * @param fieldToFormHash
	 */
	private void parseFields(String fields,
			HashMap<Integer, SimpleFieldType> typeHash,
			HashMap<Integer, Vector<Field>> fieldToFormHash) {
		//ok, let's get the fields
		try {
			JSONArray fieldsarray = new JSONArray(fields);
			int arrlength = fieldsarray.length();
			for(int i = 0; i < arrlength; i++) {
				try {
					JSONObject obj = fieldsarray.getJSONObject(i);
					
					if(!obj.getString("model").equals("rapidandroid.field")) {
						assertTrue(false);
					}
					
					int pk = obj.getInt("pk");
					
					
					JSONObject jsonfields = obj.getJSONObject("fields");
					int form_id = jsonfields.getInt("form");
					//public Field(int id, int sequence, String name, String prompt, SimpleFieldType ftype) {
					Field newfield = new Field(pk, 
							jsonfields.getInt("sequence"),
							jsonfields.getString("name"), 
							jsonfields.getString("prompt"),
							typeHash.get(new Integer(jsonfields.getInt("fieldtype"))));
					
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
					assertTrue(false);
				}	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("dimagi", e.getMessage());
			assertTrue(false);
		}
	}

	/**
	 * @param types
	 * @param typeHash
	 */
	private void parseFieldTypes(String types,
			HashMap<Integer, SimpleFieldType> typeHash) {
		//ok, let's get the field types
//		HashMap <String, String> hackRegexHash = new HashMap<String, String>();
//		
//		hackRegexHash.put("boolean","^(t|f|true|false|y|no|yes|n|n0)(\\s|$)");
//		hackRegexHash.put("length","^(\\d+)(\\s*(cm|m))($|\\s)");
//		hackRegexHash.put("ratio","^((\\d+\\:\\d+)|(\\d+\\/\\d+)|(\\d+\\s*%)|(\\d+\\s*pct))");
//		hackRegexHash.put("height","^(\\d+)(\\s*(cm|mm|meter|meters))($|\\s)");
//		hackRegexHash.put("weight","^(\\d+)(\\s*(kg|kilo|kilos))");
//		hackRegexHash.put("number","^(\\d+)($|\\s)");
//		hackRegexHash.put("word","^([A-Za-z]+)($|\\s)");
		
		try {
			JSONArray typesarray = new JSONArray(types);
			
			int arrlength = typesarray.length();
			for(int i = 0; i < arrlength; i++) {
				try {
					JSONObject obj = typesarray.getJSONObject(i);
					Log.d("dimagi", "type loop: " + i + " model: " + obj.getString("model"));
					if(!obj.getString("model").equals("rapidandroid.fieldtype")) {
						//System.out.println(obj.get("model"));
						Log.d("dimagi", "###" + obj.getString("model")+ "###");
						assertTrue(false);
					}
					
					int pk = obj.getInt("pk");
					JSONObject jsonfields = obj.getJSONObject("fields");					
					//Log.d("dimagi", "#### Parsing SimpleFieldType: " + jsonfields.getString("name") + " [" + hackRegexHash.get(jsonfields.getString("name")) + "]");
					Log.d("dimagi", "#### Regex from file: " + jsonfields.getString("name") + " [" + jsonfields.getString("regex") + "]");
					//SimpleFieldType newtype = new SimpleFieldType(pk, jsonfields.getString("datatype"),hackRegexHash.get(jsonfields.getString("name")),jsonfields.getString("name"));
					SimpleFieldType newtype = new SimpleFieldType(pk, jsonfields.getString("datatype"),jsonfields.getString("regex"),jsonfields.getString("name"));
					typeHash.put(new Integer(pk), newtype);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//Log.d("testBootstrapForms.typesinner", e.getMessage());
					assertTrue(false);
				}	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			//Log.d("testBootstrapForms.typesouter", e.getMessage());
			assertTrue(false);
		}
	}
	
	private void insertFormsIntoDb(Vector<Form> forms) {
		//ok, now, let's create all the content types and such one by one.
		ModelTranslator.ClearFormTables();
		
		Log.d("dimagi","** inserting forms into db");
		for(int i = 0; i < forms.size(); i++) {
			Form f = forms.get(i);
			Field[] fields = f.getFields();
			Log.d("dimagi","**** inserting form " + f.getFormName());
			
			//insert the form first
			Uri formUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + f.getFormId());
			Cursor crform = getContext().getContentResolver().query(formUri, null, null, null, null);
			if (crform.getCount() == 0) {
				ContentValues typecv = new ContentValues();

				typecv.put(RapidSmsDataDefs.Form._ID, f.getFormId());
				typecv.put(RapidSmsDataDefs.Form.FORMNAME, f.getFormName());
				//typecv.put(RapidSmsDataDefs.Form.PARSEMETHOD, f.getParser().getName());	
				typecv.put(RapidSmsDataDefs.Form.PARSEMETHOD, "simpleregex");
				typecv.put(RapidSmsDataDefs.Form.PREFIX, f.getPrefix());
				typecv.put(RapidSmsDataDefs.Form.DESCRIPTION, f.getDescription());							

				Uri insertedFormUri = getContext().getContentResolver().insert(RapidSmsDataDefs.Form.CONTENT_URI, typecv);
				Log.d("dimagi","****** Inserted form into db: " + insertedFormUri);
				assertEquals(insertedFormUri.getPathSegments().get(1),f.getFormId()+"");					
			}		
			crform.close();
			
			Log.d("dimagi","****** Begin fields loop: " + fields.length);
			for(int j = 0; j < fields.length; j++) {
				Field thefield = fields[j];
				SimpleFieldType thetype = (SimpleFieldType) thefield.getFieldType();
				//make the URI and insert for the Fieldtype
				Log.d("dimagi","******** Iterating through fields: " + thefield.getName() + " id: " + thefield.getFieldId());

				Uri fieldtypeUri = Uri.parse(RapidSmsDataDefs.FieldType.CONTENT_URI_STRING + thetype.getId());
				Cursor typeCursor = getContext().getContentResolver().query(fieldtypeUri, null, null, null, null);
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

					Uri insertedTypeUri = getContext().getContentResolver().insert(RapidSmsDataDefs.FieldType.CONTENT_URI, typecv);
					Log.d("dimagi","********** Inserted SimpleFieldType into db: " + insertedTypeUri);
					assertEquals(insertedTypeUri.getPathSegments().get(1),thetype.getId()+"");					
				}
				typeCursor.close();
				
				Uri fieldUri = Uri.parse(RapidSmsDataDefs.Field.CONTENT_URI_STRING + thefield.getFieldId());
				Cursor crfield = getContext().getContentResolver().query(fieldUri, null, null, null, null);
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

					Uri insertedFieldUri = getContext().getContentResolver().insert(RapidSmsDataDefs.Field.CONTENT_URI, typecv);
					Log.d("dimagi","********** Inserted Field into db: " + insertedFieldUri);
					assertEquals(insertedFieldUri.getPathSegments().get(1),thefield.getFieldId()+"");					
				}			
				crfield.close();
				//next, make the uri and insert for the field.
			}
		}		
		assertEquals(2, forms.size());
		Log.d("dimagi","Test form insert success");
		
		
	}
	
	public void test004GetFormsFromDBAndPutIntoModel() {
		//Regenerate the form definitions
		test000BootstrapFormsAndInsertIntoDB();
		
		Log.d("dimagi","************ getting forms from the db");
		Uri query = RapidSmsDataDefs.Form.CONTENT_URI;
		Cursor cr = getContext().getContentResolver().query(query, null, null, null, null);
		assertEquals(2, cr.getCount());
		
		//next get the ids
		
		
		cr.moveToFirst();
		
		do {
			int id = cr.getInt(0);	//presumably the id
			Uri directUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + id);
			Log.d("dimagi", "Querying for form: " + directUri);
			Form f = ModelTranslator.getFormFromUri(getContext(), directUri);
			
			assertNotNull(f);
			assertNotNull(f.getFields());
		} while (cr.moveToNext());		
		cr.close();
	}
	
	public void test005RegenerateTablesForForms() {
		//todo:  blow away the formdata tables
		//recreate the tables from the form definition					
		
		test000BootstrapFormsAndInsertIntoDB();
					
		Uri query = RapidSmsDataDefs.Form.CONTENT_URI;
		Cursor cr = getContext().getContentResolver().query(query, null, null, null, null);
		cr.moveToFirst();
		
		do {
			int id = cr.getInt(0);	//presumably the id
			Uri directUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + id);
			
			Form f = ModelTranslator.getFormFromUri(getContext(), directUri);
			Log.d("dimagi", "Generating formData table for form: " + f.getFormName());
			ModelTranslator.generateFormTable(f);			
		} while (cr.moveToNext());		
		//mProv.ClearFormDataDebug();	//see if this crashes
		cr.close();
	}
	
	
	private void test006InsertDummyFormData() {
		
		test005RegenerateTablesForForms();
		
		Uri query = RapidSmsDataDefs.Form.CONTENT_URI;
		Cursor cr = getContext().getContentResolver().query(query, null, null, null, null);
		cr.moveToFirst();
		
		
		do {
			
			int id = cr.getInt(0);	//presumably the id			
			Uri directUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + id);			
			Form f = ModelTranslator.getFormFromUri(getContext(), directUri);			
			
			for (int msgcount = 0; msgcount < 10; msgcount++) {
				// first, let's make a new dummy message:

//				doSendMessage("test message " + msgcount, "10/31/2008 11:0"
//						+ msgcount, "6176453236");
//				String msgid = currUri.getPathSegments().get(1);
				String msgid = "1";
				
				ContentValues cv = new ContentValues();
				cv.put(RapidSmsDataDefs.FormData.MESSAGE, msgid);
				Field[] fields = f.getFields();
				int len = fields.length;
				Random r = new Random();
				
				for(int i = 0; i < len; i++) {
					Field field = fields[i];
					if (field.getFieldType().getItemType().equals("integer")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),r.nextInt(10000));
					} else if (field.getFieldType().getItemType().equals("number")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),r.nextInt(10000));
					} else if (field.getFieldType().getItemType().equals("boolean")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),r.nextBoolean());
					} else if (field.getFieldType().getItemType().equals("word")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),Math.random() + "");
					} else if (field.getFieldType().getItemType().equals("ratio")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),Math.random() + "");
					} else if (field.getFieldType().getItemType().equals("datetime")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),"10/31/2008 11:59");
					}					
				}	
				Uri inserted = getContext().getContentResolver().insert(Uri.parse(RapidSmsDataDefs.FormData.CONTENT_URI_PREFIX + f.getFormId()), cv);
				Log.d("dimagi", "inserted form data for: " + inserted);
			}
			
			
						
		} while (cr.moveToNext());
		cr.close();
	}	
}

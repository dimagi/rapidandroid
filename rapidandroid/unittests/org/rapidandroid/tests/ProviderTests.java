/**
 * 
 */
package org.rapidandroid.tests;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rapidandroid.data.ModelConverter;
import org.rapidandroid.data.RapidSmsContentProvider;
import org.rapidandroid.data.RapidSmsDataDefs;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.FieldType;
import org.rapidsms.java.core.model.Form;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.mock.MockContentResolver;
import android.util.Log;
/**
 *  
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 14, 2009
 * Description:  Main unit test to verify functionality of the RapidSmsContentProviders * 
 *   
 */

public class ProviderTests extends
		android.test.ProviderTestCase<RapidSmsContentProvider> {

	private RapidSmsContentProvider mProv;
	private Uri currUri;
	
	public ProviderTests() {
		super(RapidSmsContentProvider.class,
				"org.rapidandroid.rapidandroiddata");
	}

	protected void setUp() throws Exception {
		super.setUp();
		mProv = getProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		//System.out.println("Teardown: Clear Message tables: " +	mProv.delete(RapidSmsDataDefs.Message.CONTENT_URI, null,null));
		//System.out.println("Teardown: Clear Message tables: " +	mProv.delete(RapidSmsDataDefs.Monitor.CONTENT_URI, null,null));
		
	}

	public void testSimple() {
		assertFalse(false);
	}
	
	
	//First level bootstrap of Form definitions into DB.
	public void test000BootstrapFormsAndInsertIntoDB() {
		mProv.ClearDebug();		
		
		String fields = "[{\"pk\": 1, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 1, \"prompt\": \"Location of distribution center\", \"name\": \"Location\", \"form\": 1, \"sequence\": 1}}, {\"pk\": 2, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 2, \"prompt\": \"Number of bednets received\", \"name\": \"received\", \"form\": 1, \"sequence\": 2}}, {\"pk\": 3, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 2, \"prompt\": \"Number of bednets that have been handed out\", \"name\": \"given\", \"form\": 1, \"sequence\": 3}}, {\"pk\": 4, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 2, \"prompt\": \"Number of bednets that remain in balance\", \"name\": \"balance\", \"form\": 1, \"sequence\": 4}}, {\"pk\": 5, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 1, \"prompt\": \"Child Identifier (6 digits)\", \"name\": \"child_id\", \"form\": 2, \"sequence\": 1}}, {\"pk\": 6, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 3, \"prompt\": \"weight\", \"name\": \"weight\", \"form\": 2, \"sequence\": 2}}, {\"pk\": 7, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 4, \"prompt\": \"height\", \"name\": \"height\", \"form\": 2, \"sequence\": 3}}, {\"pk\": 8, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 5, \"prompt\": \"ratio\", \"name\": \"ratio\", \"form\": 2, \"sequence\": 4}}, {\"pk\": 9, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 6, \"prompt\": \"muac\", \"name\": \"muac\", \"form\": 2, \"sequence\": 5}}, {\"pk\": 10, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 7, \"prompt\": \"Does child suffer from oedema\", \"name\": \"oedema\", \"form\": 2, \"sequence\": 6}}, {\"pk\": 11, \"model\": \"rapidandroid.field\", \"fields\": {\"fieldtype\": 7, \"prompt\": \"does the child suffer from diarrhoea\", \"name\": \"diarrhoea\", \"form\": 2, \"sequence\": 7}}]";
		String forms = "[{\"pk\": 1, \"model\": \"rapidandroid.form\", \"fields\": {\"parsemethod\": \"simpleregex\", \"prefix\": \"bednets\", \"description\": \"Bednet Distribution(supply)\", \"formname\": \"bednets\"}}, {\"pk\": 2, \"model\": \"rapidandroid.form\", \"fields\": {\"parsemethod\": \"simpleregex\", \"prefix\": \"nutrition\", \"description\": \"Nutrition Information (monitorin and evaluation)\", \"formname\": \"Nutrition\"}}]";
		String types = "[{\"pk\": 1, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"word\", \"regex\": \"\\w+\", \"name\": \"word\"}}, {\"pk\": 2, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"number\", \"regex\": \"\\d+\", \"name\": \"number\"}}, {\"pk\": 3, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"integer\", \"regex\": \"(\\d+{1,3})(?c:\\s*(kg|kilo|kilos|lb|lbs|pounds))\", \"name\": \"weight\"}}, {\"pk\": 4, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"integer\", \"regex\": \"(\\d+{1,3})(?:\\s*(cm|m|in))\", \"name\": \"height\"}}, {\"pk\": 5, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"ratio\", \"regex\": \"(\\d+\\:\\d+)|(\\d+\\/\\d+)\", \"name\": \"ratio\"}}, {\"pk\": 6, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"integer\", \"regex\": \"(\\d+{1,3})(?:\\s*(cm|mm|m|in|ft|feet|meter|meters))\", \"name\": \"length\"}}, {\"pk\": 7, \"model\": \"rapidandroid.fieldtype\", \"fields\": {\"datatype\": \"boolean\", \"regex\": \"(t|f|true|false|y|n|yes|no)\", \"name\": \"boolean\"}}]";
		
		String formdefs = "";		
		
		HashMap<Integer, FieldType> typeHash = new HashMap<Integer,FieldType>();
		HashMap<Integer, Field> fieldHash = new HashMap<Integer,Field>();
		HashMap<Integer, Form> formHash = new HashMap<Integer,Form>();
		HashMap<Integer, Vector<Field>> fieldToFormHash = new HashMap<Integer,Vector<Field>> ();
		Vector<Form> allforms = new Vector<Form>();
		
		boolean fail  = false;
		
		parseFieldTypes(types, typeHash);		
		parseFields(fields, typeHash, fieldToFormHash);		
		parseForms(forms, fieldToFormHash, allforms);
						
		assertEquals(2, allforms.size());
		Log.d("dimagi","Test bootstrap success");
		
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
												jsonfields.getString("parsemethod"),
												fieldarr);
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
			HashMap<Integer, FieldType> typeHash,
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
					//public Field(int id, int sequence, String name, String prompt, FieldType ftype) {
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
			HashMap<Integer, FieldType> typeHash) {
		//ok, let's get the field types
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
					FieldType newtype = new FieldType(pk, jsonfields.getString("datatype"),jsonfields.getString("regex"),jsonfields.getString("name"));
					typeHash.put(new Integer(pk), newtype);
					//Log.d("dimagi", "#### Parsed FieldType: " + newtype.getId() + " [" + newtype.getName() + "]");
					
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
		Log.d("dimagi","** inserting forms into db");
		for(int i = 0; i < forms.size(); i++) {
			Form f = forms.get(i);
			Field[] fields = f.getFields();
			Log.d("dimagi","**** inserting form " + f.getFormName());
			
			//insert the form first
			Uri formUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + f.getFormId());
			Cursor crform = mProv.query(formUri, null, null, null, null);
			if (crform.getCount() == 0) {
				ContentValues typecv = new ContentValues();

				typecv.put(RapidSmsDataDefs.Form._ID, f.getFormId());
				typecv.put(RapidSmsDataDefs.Form.FORMNAME, f.getFormId());
				typecv.put(RapidSmsDataDefs.Form.PARSEMETHOD, f.getParser().getName());	
				typecv.put(RapidSmsDataDefs.Form.PREFIX, f.getPrefix());
				typecv.put(RapidSmsDataDefs.Form.DESCRIPTION, f.getDescription());							

				Uri insertedFormUri = mProv.insert(RapidSmsDataDefs.Form.CONTENT_URI, typecv);
				Log.d("dimagi","****** Inserted form into db: " + insertedFormUri);
				assertEquals(insertedFormUri.getPathSegments().get(1),f.getFormId()+"");					
			}				
			
			Log.d("dimagi","****** Begin fields loop: " + fields.length);
			for(int j = 0; j < fields.length; j++) {
				Field thefield = fields[j];
				FieldType thetype = thefield.getFieldType();
				//make the URI and insert for the Fieldtype
				Log.d("dimagi","******** Iterating through fields: " + thefield.getName() + " id: " + thefield.getFieldId());

				Uri fieldtypeUri = Uri.parse(RapidSmsDataDefs.FieldType.CONTENT_URI_STRING + thetype.getId());
				Cursor typeCursor = mProv.query(fieldtypeUri, null, null, null, null);
				if (typeCursor.getCount() == 0) {
					ContentValues typecv = new ContentValues();

					typecv.put(RapidSmsDataDefs.FieldType._ID, thetype.getId());
					typecv.put(RapidSmsDataDefs.FieldType.DATATYPE, thetype
							.getDataType());
					typecv.put(RapidSmsDataDefs.FieldType.NAME, thetype
							.getName());
					typecv.put(RapidSmsDataDefs.FieldType.REGEX, thetype
							.getRegex());

					Uri insertedTypeUri = mProv.insert(RapidSmsDataDefs.FieldType.CONTENT_URI, typecv);
					Log.d("dimagi","********** Inserted FieldType into db: " + insertedTypeUri);
					assertEquals(insertedTypeUri.getPathSegments().get(1),thetype.getId()+"");					
				}
				
				Uri fieldUri = Uri.parse(RapidSmsDataDefs.Field.CONTENT_URI_STRING + thefield.getFieldId());
				Cursor crfield = mProv.query(fieldUri, null, null, null, null);
				if (crfield.getCount() == 0) {
					ContentValues typecv = new ContentValues();

					typecv.put(RapidSmsDataDefs.Field._ID, thefield.getFieldId());
					typecv.put(RapidSmsDataDefs.Field.NAME, thefield.getName());
					typecv.put(RapidSmsDataDefs.Field.FORM, f.getFormId());
					typecv.put(RapidSmsDataDefs.Field.PROMPT, thefield.getPrompt());
					typecv.put(RapidSmsDataDefs.Field.SEQUENCE, thefield.getSequenceId());
					typecv.put(RapidSmsDataDefs.Field.FIELDTYPE, thefield.getFieldType().getId());	
					
//					Log.d("dimagi", "_ID: " + thefield.getFieldId());
//					Log.d("dimagi", "NAME: " + thefield.getName());
//					Log.d("dimagi", "FORM: " + f.getFormId());
//					Log.d("dimagi", "PROMPT: " + thefield.getPrompt());
//					Log.d("dimagi", "SEQUENCE: " + thefield.getSequenceId());
//					Log.d("dimagi", "FIELDTYPE: " + thefield.getFieldType().getId());

					Uri insertedFieldUri = mProv.insert(RapidSmsDataDefs.Field.CONTENT_URI, typecv);
					Log.d("dimagi","********** Inserted Field into db: " + insertedFieldUri);
					assertEquals(insertedFieldUri.getPathSegments().get(1),thefield.getFieldId()+"");					
				}				
				//next, make the uri and insert for the field.
			}
		}		
		assertEquals(2, forms.size());
		Log.d("dimagi","Test form insert success");
		
		
	}
	

	public void test001ProviderGetTypeBase() {
		assertEquals(mProv.getType(RapidSmsDataDefs.Message.CONTENT_URI),RapidSmsDataDefs.Message.CONTENT_TYPE);
		assertEquals(mProv.getType(RapidSmsDataDefs.Monitor.CONTENT_URI),RapidSmsDataDefs.Monitor.CONTENT_TYPE);		
		
		assertEquals(mProv.getType(RapidSmsDataDefs.Form.CONTENT_URI),RapidSmsDataDefs.Form.CONTENT_TYPE);
		assertEquals(mProv.getType(RapidSmsDataDefs.Field.CONTENT_URI),RapidSmsDataDefs.Field.CONTENT_TYPE);		
		assertEquals(mProv.getType(RapidSmsDataDefs.FieldType.CONTENT_URI),RapidSmsDataDefs.FieldType.CONTENT_TYPE);
		
		//assertEquals(mProv.getType(RapidSmsDataDefs.FormData.CONTENT_URI),RapidSmsDataDefs.FormData.CONTENT_TYPE);	//this doesn';t exist in this case				
	}
	
	public void test001ProviderGetTypeID() {
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.Message.URI_PART + "/1")),RapidSmsDataDefs.Message.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.Monitor.URI_PART + "/1")),RapidSmsDataDefs.Monitor.CONTENT_ITEM_TYPE);		
		
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.Form.URI_PART + "/1")),RapidSmsDataDefs.Form.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.Field.URI_PART + "/1")),RapidSmsDataDefs.Field.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.FieldType.URI_PART + "/1")),RapidSmsDataDefs.FieldType.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/" + RapidSmsDataDefs.FormData.URI_PART + "/1")),RapidSmsDataDefs.FormData.CONTENT_TYPE);
	}	
	
	public void test002MonitorInsertAndQuerySingle() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDataDefs.Monitor.PHONE,"6176453236");		
		currUri = mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
		
		Cursor cr = mProv.query(currUri, null, null, null, null);
		assertEquals(1, cr.getCount());
		cr.close();
	}
	
	public void test002MonitorInsertRepeat() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDataDefs.Monitor.PHONE,"6176453236");		
		currUri = mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
				
		Uri repeatUri = mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
		
		assertEquals(repeatUri,currUri);
						
	}
	
	public void test002MonitorInsertAndVerifyCounts() {
		int count = 10;
		int baseline = 0;
		//Log.w("ProviderTests.testMonitorInsertAndVerifyCounts", "flasjdfklasdjf");
		Uri monitorquery = Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/monitor");
		Cursor cr = mProv.query(monitorquery, null, null, null, null);
		baseline = cr.getCount();
		
		for(int i = 0; i < count; i++) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(RapidSmsDataDefs.Monitor.PHONE,"8887" + i);		
			mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
		}
		
		Cursor cr2 =mProv.query(monitorquery, null, null, null, null);
		assertEquals(baseline+count, cr2.getCount());		
        
	}
	
	String too_long = "Alert in golaoda werda no of otps only 1,other new 5 otps to start in plan.Problem in thise werda shortage of f100and75,traind manpowe,tranport do to thisez program for 1month stop but now already start it.";
	
	//add a bunch and confirm that the number of messages are ok
	//add messages as well and see if the number of 
	public void test002MessageInsertMessage() {
		String msg1 = "alert unlocked supply room at WSMA";
		String date1 = "10/30/2008 19:51";
		String phone1 = "251912149840";		
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN WKIG  23   0  0  0  PN OOZM  5  0  0  0 PN VHPF 5  0  0  0 PN XDQQ 6 0  0  0";
		date1 = "10/31/2008 11:50";
		phone1 = "251913086827";		
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN WKIG  23   0  0  0";
		date1 = "10/31/2008 11:51";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN OOZM  5  0  0  0";
		date1 = "10/31/2008 11:52";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN VHPF 5  0  0  0";
		date1 = "10/31/2008 11:53";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "Alert problem in meyu werda shortage of transport and pn because of this pn distrbution for 2wk stop but now started.In thise werda no of otps are 6.";
		date1 = "10/31/2008 11:58";
		phone1 = "251911877430";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "CANCEL TO  CHIROO WOREDA";
		date1 = "10/31/2008 12:58";
		phone1 = "251911877430";		
		doSendMessage(msg1, date1, phone1);
		
		
		//confirm the numbers
		Cursor msgcount = mProv.query(RapidSmsDataDefs.Message.CONTENT_URI, null, null, null, null);
		assertEquals(7,msgcount.getCount());
		
		Cursor monitorcount = mProv.query(RapidSmsDataDefs.Monitor.CONTENT_URI, null, null, null, null);
		assertEquals(3,monitorcount.getCount());
				
	}
	
	private void doSendMessage(String msg, String date, String phone) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDataDefs.Message.MESSAGE,msg);		
		initialValues.put(RapidSmsDataDefs.Message.PHONE,phone);
		initialValues.put(RapidSmsDataDefs.Message.TIME,date);
		initialValues.put(RapidSmsDataDefs.Message.IS_OUTGOING,false);
		currUri = mProv.insert(RapidSmsDataDefs.Message.CONTENT_URI, initialValues);
	}
	
	public void test003InsertMessagesAndCountPerMonitor() {
		String msg1 = "alert unlocked supply room at WSMA";
		String date1 = "10/30/2008 19:51";
		String phone1 = "251912149840";		
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN WKIG  23   0  0  0  PN OOZM  5  0  0  0 PN VHPF 5  0  0  0 PN XDQQ 6 0  0  0";
		date1 = "10/31/2008 11:50";
		phone1 = "251913086827";		
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN WKIG  23   0  0  0";
		date1 = "10/31/2008 11:51";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN OOZM  5  0  0  0";
		date1 = "10/31/2008 11:52";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "PN VHPF 5  0  0  0";
		date1 = "10/31/2008 11:53";
		phone1 = "251913086827";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "Alert problem in meyu werda shortage of transport and pn because of this pn distrbution for 2wk stop but now started.In thise werda no of otps are 6.";
		date1 = "10/31/2008 11:58";
		phone1 = "251911877430";
		doSendMessage(msg1, date1, phone1);
		
		msg1 = "CANCEL TO  CHIROO WOREDA";
		date1 = "10/31/2008 12:58";
		phone1 = "251911877430";		
		doSendMessage(msg1, date1, phone1);
		
		//251912149840 = 1
		//251913086827 = 4
		//251911877430 = 2
		//total should be 7
		
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251912149840"), 1);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251913086827"), 4);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251911877430"), 2);	
		
		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251912149840"));
		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251913086827"));
		deleteMessagesByMonitor(getMonitorIdForPhoneNumber("251911877430"));
		
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251912149840"),0);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251913086827"),0);
		confirmMessageCountForMonitor(getMonitorIdForPhoneNumber("251911877430"),0);		
	}
	
	private int getMonitorIdForPhoneNumber(String phonenumber) {
		Cursor monitor = mProv.query(RapidSmsDataDefs.Monitor.CONTENT_URI, null, "phone='" + phonenumber + "'", null,null);
		monitor.moveToFirst();
		assertEquals(1, monitor.getCount());
		int ret = monitor.getInt(0);
		monitor.close();
		return ret;
	}
	
	private void confirmMessageCountForMonitor(int monitor_id, int expected) {
		Uri getMessagesBySingleMonitor = ContentUris.withAppendedId(RapidSmsDataDefs.Monitor.MESSAGE_BY_MONITOR_URI, monitor_id);				
		Cursor msgcount = mProv.query(getMessagesBySingleMonitor, null, null, null, null);
		assertEquals(expected,msgcount.getCount());
		msgcount.close();
	}
	
	private void deleteMessagesByMonitor(int monitor_id) {
		Cursor msgcount = mProv.query(RapidSmsDataDefs.Message.CONTENT_URI, null, null, null, null);
		int oldcount = msgcount.getCount();
		msgcount.close();
		
		Uri deleteMessagesBySingleMonitor = ContentUris.withAppendedId(RapidSmsDataDefs.Monitor.MESSAGE_BY_MONITOR_URI, monitor_id);
		int delcount = mProv.delete(deleteMessagesBySingleMonitor, null, null);
		
		Cursor newcountc = mProv.query(RapidSmsDataDefs.Message.CONTENT_URI, null, null, null, null);
		int newcount = newcountc.getCount();
		
		assertEquals(newcount+delcount,oldcount);
		//System.out.println("deleteMessagesByMonitor: " + monitor_id + " oldcount: " + oldcount + " delcount: " + delcount + " newcount: " + newcount);		 
	}
	
	
	
	public void test004GetFormsFromDBAndPutIntoModel() {
		//Regenerate the form definitions
		test000BootstrapFormsAndInsertIntoDB();
		
		Log.d("dimagi","************ getting forms from the db");
		Uri query = RapidSmsDataDefs.Form.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
		assertEquals(2, cr.getCount());
		
		//next get the ids
		
		
		cr.moveToFirst();
		
		do {
			int id = cr.getInt(0);	//presumably the id
			Uri directUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + id);
			Log.d("dimagi", "Querying for form: " + directUri);
			Form f = ModelConverter.getFormFromUri(mProv, directUri);
			
			assertNotNull(f);
			assertNotNull(f.getFields());
		} while (cr.moveToNext());		
	}
	
	public void testGetFieldTypes() {
		Uri query = RapidSmsDataDefs.FieldType.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
	}	
	
	public void testGetFields() {
		Uri query = RapidSmsDataDefs.Field.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
	}

	public void test005RegenerateTablesForForms() {
		//todo:  blow away the formdata tables
		//recreate the tables from the form definition					
		
		test000BootstrapFormsAndInsertIntoDB();
					
		try {
			mProv.ClearFormDataDebug();
		} catch (Exception ex) {
			//this is ok if they don't exist
		}
		
		Uri query = RapidSmsDataDefs.Form.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
		cr.moveToFirst();
		
		do {
			int id = cr.getInt(0);	//presumably the id
			Uri directUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + id);
			
			Form f = ModelConverter.getFormFromUri(mProv, directUri);
			Log.d("dimagi", "Generating formData table for form: " + f.getFormName());
			mProv.generateFormTable(f);			
		} while (cr.moveToNext());		
		//mProv.ClearFormDataDebug();	//see if this crashes		
	}
	
	public void test006InsertDummyFormData() {
		
		test005RegenerateTablesForForms();
		
		Uri query = RapidSmsDataDefs.Form.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
		cr.moveToFirst();
		
		
		do {
			
			int id = cr.getInt(0);	//presumably the id			
			Uri directUri = Uri.parse(RapidSmsDataDefs.Form.CONTENT_URI_STRING + id);			
			Form f = ModelConverter.getFormFromUri(mProv, directUri);			
			
			for (int msgcount = 0; msgcount < 10; msgcount++) {
				// first, let's make a new dummy message:

				doSendMessage("test message " + msgcount, "10/31/2008 11:0"
						+ msgcount, "6176453236");
				String msgid = currUri.getPathSegments().get(1);
				
				ContentValues cv = new ContentValues();
				cv.put(RapidSmsDataDefs.FormData.MESSAGE, msgid);
				Field[] fields = f.getFields();
				int len = fields.length;
				Random r = new Random();
				
				for(int i = 0; i < len; i++) {
					Field field = fields[i];
					if (field.getFieldType().getDataType().equals("integer")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),r.nextInt(10000));
					} else if (field.getFieldType().getDataType().equals("number")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),r.nextInt(10000));
					} else if (field.getFieldType().getDataType().equals("boolean")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),r.nextBoolean());
					} else if (field.getFieldType().getDataType().equals("word")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),Math.random() + "");
					} else if (field.getFieldType().getDataType().equals("ratio")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),Math.random() + "");
					} else if (field.getFieldType().getDataType().equals("datetime")) {
						cv.put(RapidSmsDataDefs.FormData.COLUMN_PREFIX + field.getName(),"10/31/2008 11:59");
					}					
				}	
				Uri inserted = mProv.insert(Uri.parse(RapidSmsDataDefs.FormData.CONTENT_URI_PREFIX + f.getFormId()), cv);
				Log.d("dimagi", "inserted form data for: " + inserted);
			}
			
			
						
		} while (cr.moveToNext());
	}	

	public void testGetFormData() {
		// objective:
		// get all the forms from the database
		Uri query = RapidSmsDataDefs.Form.CONTENT_URI;
		Cursor cr = mProv.query(query, null, null, null, null);
		if (cr.getCount() > 0) {
			cr.moveToFirst();
			do {
				// using all the ids from the form
				// do queries off the tables in the database
				int formId = cr.getInt(0); // get the id
				Uri formDataUri = Uri
						.parse(RapidSmsDataDefs.FormData.CONTENT_URI_PREFIX
								+ formId);
				Cursor formDataCursor = mProv.query(formDataUri, null, null,
						null, null);

				formDataCursor.close();
			} while (cr.moveToNext() && cr.getCount() > 0);
		}
		cr.close();
	}
	
	
	
	
	
}

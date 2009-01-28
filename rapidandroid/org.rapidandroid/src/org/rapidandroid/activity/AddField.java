/**
 * 
 */
package org.rapidandroid.activity;


import org.rapidandroid.R;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidsms.java.core.model.SimpleFieldType;
import org.rapidsms.java.core.parser.token.ITokenParser;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009 
 * 			
 * 			Activity window for adding a new field instance. It is
 *          a simple view with a pulldown for for field type, as well as some
 *          text entry for the field properties.
 * 
 *          Its structure should reflect the properties of
 *          org.rapidsms.java.core.model.Field
 * 
 */

public class AddField extends Activity {

	String[] existingFields;
	ITokenParser[] fieldTypes;
	String[] fieldTypeNames;
	
	public static final String RESULT_FIELDNAME = "fieldname";
	public static final String RESULT_PROMPT  = "fieldname";
	public static final String RESULT_FIELDTYPE_ID = "fieldtypeid";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_field);
		this.setTitle("Add field");		
		loadFieldTypes();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			existingFields = extras.keySet().toArray(new String[extras.keySet().size()]);
		}
		
		
		Button btnAdd = (Button) findViewById(R.id.btnAddField_add);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(saveNewField()) {
					EditText etxName = (EditText)findViewById(R.id.etx_fieldname);
					EditText etxPrompt = (EditText)findViewById(R.id.etx_fieldprompt);
					Spinner spinnerFieldTypes = (Spinner)findViewById(R.id.cbx_fieldtype);
					
					Intent ret = new Intent();
					ret.putExtra(RESULT_FIELDNAME, etxName.getText().toString());
					ret.putExtra(RESULT_PROMPT, etxPrompt.getText().toString());
					int pos = spinnerFieldTypes.getSelectedItemPosition();
					int fieldTypeIdHack = ((SimpleFieldType) fieldTypes[pos]).getId();					
					ret.putExtra(RESULT_FIELDTYPE_ID, fieldTypeIdHack);
					setResult(FormCreator.ACTIVITY_ADDFIELD_ADDED, ret);
					finish();
				}
			}
			
		});
		Button btnCancel = (Button) findViewById(R.id.btnAddField_Cancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(FormCreator.ACTIVITY_ADDFIELD_CANCEL);
				finish();				
			}
			
		});		
	}
	
	private void loadFieldTypes() {
		Spinner spinnerFieldTypes = (Spinner)findViewById(R.id.cbx_fieldtype);
		if(fieldTypes == null) {
			fieldTypes = ModelTranslator.getFieldTypes();
			int typeLen = fieldTypes.length;
			fieldTypeNames = new String[typeLen];
			for(int i = 0; i < typeLen; i++) {
				fieldTypeNames[i] = fieldTypes[i].getTokenName();
			}
		}
		//simple_spinner_dropdown_item
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, fieldTypeNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerFieldTypes.setAdapter(adapter);
		//get the field types from the fieldhash baby
	}
	
	private boolean saveNewField() {
		EditText etxName = (EditText)findViewById(R.id.etx_fieldname);
		TextView lblFieldName = (TextView)findViewById(R.id.lbl_fieldname);
		TextView lblFieldPrompt = (TextView)findViewById(R.id.lbl_fieldprompt);
		
		if(etxName.getText().length() == 0) {			
			lblFieldName.setText("Name: * Required field");
			//lblFieldName.setTextColor(R.color.solid_red);
			return false;
		} else {
			//ugly validation logic
			lblFieldName.setText("Name:");
		}
		EditText etxPrompt = (EditText)findViewById(R.id.etx_fieldprompt);
		if(etxPrompt.getText().length() == 0) {
			
			lblFieldPrompt.setText("Prompt: * Required field");
			//lblFieldPrompt.setTextColor(R.color.solid_red);
			return false;
		} else {
			//ugly validation logic
			lblFieldPrompt.setText("Prompt:");
		}
		//iterate over the prior created fieldnames from teh bundle
		if(existingFields != null) {
			String name = etxName.getText().toString();
			boolean duplicate = false;
			int len = existingFields.length;
			for(int i = 0; i < len; i++) {
				if(name.equals(existingFields[i])) {
					duplicate = true;
					break;
				}
			}
			if(duplicate) {
				lblFieldName.setText("* Field name must be unique to this form");
				return false;
			}
		}		
		return true;
	}
}

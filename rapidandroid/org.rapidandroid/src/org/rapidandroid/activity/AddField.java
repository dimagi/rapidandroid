/*
 * Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

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

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 *          Activity window for adding a new field instance. It is a simple view
 *          with a pulldown for for field type, as well as some text entry for
 *          the field properties.
 * 
 *          Its structure should reflect the properties of
 *          org.rapidsms.java.core.model.Field
 * 
 */

public class AddField extends Activity {

	String[] existingFields;
	ITokenParser[] fieldTypes;
	String[] fieldTypeNames;

	public class ResultConstants {
		public static final String RESULT_KEY_FIELDNAME = "fieldname";
		public static final String RESULT_KEY_DESCRIPTION = "description";
		public static final String RESULT_KEY_FIELDTYPE_ID = "fieldtypeid";
	}

	private static final int MENU_SAVE = Menu.FIRST;
	private static final int MENU_CANCEL = Menu.FIRST + 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_field);
		setTitle("Add field");
		loadFieldTypes();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			existingFields = extras.keySet().toArray(new String[extras.keySet().size()]);
		}

		if (savedInstanceState != null) {
			EditText etxName = (EditText) findViewById(R.id.etx_fieldname);
			EditText etxPrompt = (EditText) findViewById(R.id.etx_fieldprompt);
			Spinner spinnerFieldTypes = (Spinner) findViewById(R.id.cbx_fieldtype);

			etxName.setText(savedInstanceState.getString(ResultConstants.RESULT_KEY_FIELDNAME));
			etxPrompt.setText(savedInstanceState.getString(ResultConstants.RESULT_KEY_DESCRIPTION));
			int position = savedInstanceState.getInt(ResultConstants.RESULT_KEY_FIELDTYPE_ID);
			if (position >= 0) {
				spinnerFieldTypes.setSelection(position);
			}
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		EditText etxName = (EditText) findViewById(R.id.etx_fieldname);
		EditText etxPrompt = (EditText) findViewById(R.id.etx_fieldprompt);
		Spinner spinnerFieldTypes = (Spinner) findViewById(R.id.cbx_fieldtype);

		outState.putString(ResultConstants.RESULT_KEY_FIELDNAME, etxName.getText().toString());
		outState.putString(ResultConstants.RESULT_KEY_DESCRIPTION, etxPrompt.getText().toString());
		outState.putInt(ResultConstants.RESULT_KEY_FIELDTYPE_ID, spinnerFieldTypes.getSelectedItemPosition());
	}

	private void loadFieldTypes() {
		Spinner spinnerFieldTypes = (Spinner) findViewById(R.id.cbx_fieldtype);
		if (fieldTypes == null) {
			fieldTypes = ModelTranslator.getFieldTypes();
			int typeLen = fieldTypes.length;
			fieldTypeNames = new String[typeLen];
			for (int i = 0; i < typeLen; i++) {
				fieldTypeNames[i] = fieldTypes[i].getReadableName();
			}
		}
		// simple_spinner_dropdown_item
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
																fieldTypeNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerFieldTypes.setAdapter(adapter);
		// get the field types from the fieldhash baby
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_SAVE, 0, R.string.formeditor_menu_save).setIcon(android.R.drawable.ic_menu_save);
		menu.add(0, MENU_CANCEL, 0, R.string.formeditor_menu_cancel)
			.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case MENU_SAVE:
				if (saveNewField()) {
					EditText etxName = (EditText) findViewById(R.id.etx_fieldname);
					EditText etxPrompt = (EditText) findViewById(R.id.etx_fieldprompt);
					Spinner spinnerFieldTypes = (Spinner) findViewById(R.id.cbx_fieldtype);

					Intent ret = new Intent();
					ret.putExtra(ResultConstants.RESULT_KEY_FIELDNAME, etxName.getText().toString());
					ret.putExtra(ResultConstants.RESULT_KEY_DESCRIPTION, etxPrompt.getText().toString());
					int pos = spinnerFieldTypes.getSelectedItemPosition();
					int fieldTypeIdHack = ((SimpleFieldType) fieldTypes[pos]).getId();
					ret.putExtra(ResultConstants.RESULT_KEY_FIELDTYPE_ID, fieldTypeIdHack);
					setResult(FormCreator.ACTIVITY_ADDFIELD_ADDED, ret);
					finish();
				}
				return true;
			case MENU_CANCEL:
				setResult(FormCreator.ACTIVITY_ADDFIELD_CANCEL);
				finish();
				return true;
		}

		return true;
	}

	private boolean saveNewField() {
		EditText etxName = (EditText) findViewById(R.id.etx_fieldname);
		TextView lblFieldName = (TextView) findViewById(R.id.lbl_fieldname);
		TextView lblFieldPrompt = (TextView) findViewById(R.id.lbl_fieldprompt);

		if (etxName.getText().length() == 0) {
			lblFieldName.setText("Name: * Required field");
			// lblFieldName.setTextColor(R.color.solid_red);
			return false;
		} else {
			// ugly validation logic
			lblFieldName.setText("Name:");
		}
		EditText etxPrompt = (EditText) findViewById(R.id.etx_fieldprompt);
		if (etxPrompt.getText().length() == 0) {

			lblFieldPrompt.setText("Prompt: * Required field");
			// lblFieldPrompt.setTextColor(R.color.solid_red);
			return false;
		} else {
			// ugly validation logic
			lblFieldPrompt.setText("Prompt:");
		}
		// iterate over the prior created fieldnames from teh bundle
		if (existingFields != null) {
			String name = etxName.getText().toString();
			boolean duplicate = false;
			int len = existingFields.length;
			for (int i = 0; i < len; i++) {
				if (name.equals(existingFields[i])) {
					duplicate = true;
					break;
				}
			}
			if (duplicate) {
				lblFieldName.setText("* Field name must be unique to this form");
				return false;
			}
		}
		return true;
	}
}

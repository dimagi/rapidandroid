/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.ActivityConstants;
import org.rapidandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author dmyung
 * Main place where one can edit a form.
 * 
 * a separate dialog is necessary to change the data
 */
public class FormEditorActivity extends Activity {
	private static final int MENU_SAVE = Menu.FIRST;
    private static final int MENU_CANCEL = Menu.FIRST + 1;
    private static final int MENU_ADD_FIELD = Menu.FIRST + 2;
	
    private String mScratchDescription = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_edit);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			String formname = extras.getString(ActivityConstants.EDIT_FORM);
			TextView txv_formname = (TextView)findViewById(R.id.txv_formname);
			TextView txv_prefix = (TextView)findViewById(R.id.txv_formprefix);
			TextView txv_description = (TextView)findViewById(R.id.txv_description);
			if(formname.equals("")) {
				//todo:  need to figure out how to make this an actual entry section. New dialog on top?  or completely switch the activity?
				txv_formname.setText("<enter formname>");
				txv_prefix.setText("<enter prefix>");
				txv_description.setText("<enter description>");
			} else {
				//todo:  Need to go to DB and pull out all the form info and populate the controls.
				//Spinner spin_forms = (Spinner) findViewById(R.id.cbx_forms);				
				txv_formname.setText(formname);
				txv_prefix.setText("some prefix");
				txv_description.setText(mScratchDescription);					
			}
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_SAVE,0, R.string.formeditor_menu_save);
        menu.add(0, MENU_CANCEL,0, R.string.formeditor_menu_cancel);
        menu.add(0, MENU_ADD_FIELD,0, R.string.formeditor_menu_add_field);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case MENU_SAVE:
			return true;
		case MENU_CANCEL:
//			Intent mIntent = new Intent();
//            mIntent.putExtras(bundle);
//            setResult(RESULT_OK, mIntent)
			finish();
			return true;
		case MENU_ADD_FIELD:
			//start a new activity?
			return true;		
		}		
		return true;
	}
}

/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.form_edit);
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

/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author dmyung
 * Main place where one can edit a form.
 * 
 * a separate dialog is necessary to change the data
 */
public class FormEditorActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.form_edit);
	}

}

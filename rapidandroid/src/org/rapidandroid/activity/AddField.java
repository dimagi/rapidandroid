/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_field);

		Button btn_finished = (Button) findViewById(R.id.btn_finishaddfield);
		btn_finished.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// bundle up the finishes
				finish();
			}

		});

		Button btn_cancel = (Button) findViewById(R.id.btn_canceladdfield);
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
	}
}

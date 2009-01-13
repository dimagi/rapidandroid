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
 * @author dmyung
 * @created Jan 12, 2009
 */
public class AddField extends Activity {

	/* (non-Javadoc)
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
				//bundle up the finishes
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

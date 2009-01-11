/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.R;
import org.rapidandroid.view.MessageViewAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * @author dmyung
 *
 */
public class FormReview extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.form_review);
		ListView lsv = (ListView) findViewById(R.id.lsv_messages);
		
		//MessageViewAdapter msgAdapter = new MessageViewAdapter(lsv.getContext(), R.layout.message_view);
		MessageViewAdapter msgAdapter = new MessageViewAdapter(this, R.layout.message_view);
		lsv.setAdapter(msgAdapter);
	}	

}

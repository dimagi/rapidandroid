/**
 * 
 */
package org.rapidandroid.activity;

import java.util.Random;

import org.rapidandroid.ActivityConstants;
import org.rapidandroid.R;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 *          Activity window for editing a Form.
 * 
 */


public class FormReviewer extends Activity {
	private static final int MENU_DONE = Menu.FIRST;
	private static final int MENU_FORMAT = Menu.FIRST + 1;
	

	private Form mForm;
	
	private String mScratchDescription = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_edit);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int formID = extras.getInt(ActivityConstants.REVIEW_FORM);
			mForm = ModelTranslator.getFormById(formID);
			
						
			TextView txv_formname = (TextView) findViewById(R.id.txv_formname);
			TextView txv_prefix = (TextView) findViewById(R.id.txv_formprefix);
			TextView txv_description = (TextView) findViewById(R.id.txv_description);

			ListView lsv_fields = (ListView) findViewById(R.id.lsv_fields);
			
			txv_formname.setText(mForm.getFormName());
			txv_prefix.setText(mForm.getPrefix());
			txv_description.setText(mForm.getDescription());

			int len = mForm.getFields().length;
			String[] fields = new String[len];
			for(int i =0; i < len; i++) {
				Field field =  mForm.getFields()[i];
				fields[i] = field.getName() + " [" + field.getFieldType().getItemType() + "]";
			}
			lsv_fields.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, fields));


		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_DONE, 0, R.string.formreview_menu_done).setIcon(android.R.drawable.ic_menu_revert);;
		menu.add(0, MENU_FORMAT, 0, R.string.formreview_menu_format).setIcon(android.R.drawable.ic_menu_info_details);;		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_DONE:
			finish();
			return true;
		case MENU_FORMAT:
			// Intent mIntent = new Intent();
			// mIntent.putExtras(bundle);
			// setResult(RESULT_OK, mIntent)
			showDialog(0);
			return true;
		
		}
		return true;
	}

	private static String[] bools = new String[]{"t", "f","true","false","yes","no","y","n"};
	private static String[] heights = new String[]{"cm", "m","meters","meter"};
	private static String[] lengths = new String[]{"cm", "m","meters","meter"};
	private static String[] weights = new String[]{"kg","kilos","kilo","kg.","kgs"};
	Random r = new Random();
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		
		String title = "Sample submission";
		
		
		if(mForm == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(mForm.getPrefix() + " ");
		
		Field[] fields = mForm.getFields();
		int len = fields.length;
		
		
		for(int i= 0; i < len; i++) {
			Field field = fields[i];
			
			String type = field.getFieldType().getTokenName();
			
			if(type.equals("word")) {
				String token = Long.toString(Math.abs(r.nextLong()), 36);
				sb.append(token.substring(0, 10));				
			} else if(type.equals("number")) {
				sb.append(r.nextInt(1000));
			}else if(type.equals("height")) {
				sb.append(r.nextInt(200) + " " + heights[r.nextInt(heights.length)]);
			}else if(type.equals("boolean")) {
				sb.append(bools[r.nextInt(bools.length)]);
			}else if(type.equals("length")) {
				sb.append(r.nextInt(200) + " " + lengths[r.nextInt(lengths.length)]);
			} else if (type.equals("ratio")) {
				String floatString = r.nextFloat() + "";
				sb.append(floatString.substring(0, 4));
			}
			sb.append(" ");
			
		}
		
		return new AlertDialog.Builder(FormReviewer.this).setTitle(title)
		.setMessage(sb.toString().trim()).setPositiveButton("OK", null).create();
				
	}
}

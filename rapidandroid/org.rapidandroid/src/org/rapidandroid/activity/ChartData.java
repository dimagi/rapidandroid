package org.rapidandroid.activity;

import org.rapidandroid.ActivityConstants;
import org.rapidandroid.R;
import org.rapidandroid.activity.chart.IChartBroker;
import org.rapidandroid.activity.chart.form.FormDataBroker;
import org.rapidandroid.activity.chart.message.MessageDataBroker;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 28, 2009
 * Summary:
 */
public class ChartData extends Activity {

	private static final String CHART_FILE = "file:///android_asset/flot/html/basechart.html";
	private static final String JAVASCRIPT_PROPERTYNAME = "graphdata";
	
	private static final int MENU_DONE = Menu.FIRST;
	private static final int MENU_CHANGE_VARIABLE = Menu.FIRST + 1;
	private static final int MENU_CHANGE_PARAMETERS = Menu.FIRST + 2;
	
	private Form mForm;
	private Field mField;
	IChartBroker mBroker;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		//getWindow().setTitle("Viewing Some data");	//this doesn't seem to work just yet.  may need to use the com.example.android.apis.app.CustomTitle example and the title layout.  ugh.

		setContentView(org.rapidandroid.R.layout.data_chart);
		WebView wv = (WebView) findViewById(org.rapidandroid.R.id.wv1);

		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if(extras.containsKey(ActivityConstants.CHART_FORM)) {
				mForm = ModelTranslator.getFormById(extras.getInt(ActivityConstants.CHART_FORM));
				mBroker= new FormDataBroker(wv, mForm);
			} else if(extras.containsKey(ActivityConstants.CHART_MESSAGES)) {
				mBroker= new MessageDataBroker(wv);
			} else if(extras.containsKey(ActivityConstants.CHART_MONITORS)) {
				
			}
		}
		
        
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(mBroker, JAVASCRIPT_PROPERTYNAME);
        wv.loadUrl(CHART_FILE);
     
        wv.debugDump();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		super.onCreateDialog(id);
		
		switch (id) {
			case MENU_CHANGE_VARIABLE:
				
				return new AlertDialog.Builder(ChartData.this)            
		        .setTitle("Choose Field")            
		        .setSingleChoiceItems(mBroker.getVariables(), 0, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {

		            	//mField = mForm.getFields()[whichButton];
		            	mBroker.setVariable(whichButton);
		            }
		        })
		        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {

		            	mBroker.loadGraph();
		            	//mBroker.setFieldToChart(mField);
		            }
		        })
		        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {                	
		                /* User clicked No so do some stuff */
		            }
		        })
		       .create();		
			default:
				return null;
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_DONE, 0, R.string.chart_menu_done).setIcon(android.R.drawable.ic_menu_revert);
		menu.add(0, MENU_CHANGE_VARIABLE, 0, R.string.chart_menu_change_variable).setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, MENU_CHANGE_PARAMETERS, 0, R.string.chart_menu_change_parameters).setIcon(android.R.drawable.ic_menu_recent_history);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_DONE:
			finish();
			return true;
		case MENU_CHANGE_VARIABLE:
			showDialog(MENU_CHANGE_VARIABLE);
			return true;
		case MENU_CHANGE_PARAMETERS:
			//todo, show dialog
			return true;
		}
		return true;
	}
}

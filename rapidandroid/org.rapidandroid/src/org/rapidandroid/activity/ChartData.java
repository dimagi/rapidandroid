package org.rapidandroid.activity;

import org.rapidandroid.R;
import org.rapidandroid.activity.chart.DataBroker;

import android.app.Activity;
import android.app.Dialog;
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
	
	private static final int MENU_DONE = Menu.FIRST;
	private static final int MENU_CHANGE_VARIABLE = Menu.FIRST + 1;
	private static final int MENU_CHANGE_PARAMETERS = Menu.FIRST + 2;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(org.rapidandroid.R.layout.data_chart);
		WebView wv = (WebView) findViewById(org.rapidandroid.R.id.wv1);
		
		getWindow().setTitle("Viewing Some data");	//this doesn't seem to work just yet.  may need to use the com.example.android.apis.app.CustomTitle example and the title layout.  ugh.
        
        DataBroker bd = new DataBroker(wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(bd, "graphdata");
        wv.loadUrl(CHART_FILE);
     
        wv.debugDump();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
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
			//todo, show dialog

			return true;
		case MENU_CHANGE_PARAMETERS:
			//todo, show dialog
			return true;
		}
		return true;
	}
}

/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author dmyung
 * @created Jan 12, 2009
 */
public class FormCreator extends Activity {
	private static final int MENU_SAVE = Menu.FIRST;
    private static final int MENU_ADD_FIELD = Menu.FIRST + 1;
    private static final int MENU_CANCEL = Menu.FIRST + 2;    
    
    private static final int ACTIVITY_ADDFIELD=0;
    
    private static final int CONTEXT_MOVE_UP = ContextMenu.FIRST;
    private static final int CONTEXT_MOVE_DOWN = ContextMenu.FIRST + 1;
    private static final int CONTEXT_EDIT = ContextMenu.FIRST + 2;
    private static final int CONTEXT_REMOVE = ContextMenu.FIRST + 3;	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_create);
		
		//add some events to the listview
		ListView lsv = (ListView) findViewById(R.id.lsv_createfields);
		
		//bind a context menu
		lsv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() { 
		    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		    	menu.setHeaderTitle("Edit Field");
				menu.add(0, CONTEXT_MOVE_UP, 0, "Move Up");				
				menu.add(0, CONTEXT_MOVE_DOWN, 0, "Move Down");
				menu.add(0, CONTEXT_EDIT, 0, "Edit");
				menu.add(0, CONTEXT_REMOVE, 0, "Remove");
				
			} 
		  }); 
		
		lsv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"Hair Color","Weight","age"}));
		
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_SAVE,0, R.string.formeditor_menu_save);
        menu.add(0, MENU_ADD_FIELD,0, R.string.formeditor_menu_add_field);
        menu.add(0, MENU_CANCEL,0, R.string.formeditor_menu_cancel);        
        return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case MENU_SAVE:
			//finish up the activity
			finish();
			return true;
		case MENU_ADD_FIELD:		
			Intent i = new Intent(this, AddField.class);
	        //i.putExtra("FormName", selectedFormName);	//bad form, should use some enum here        
	        startActivityForResult(i, ACTIVITY_ADDFIELD);		
			return true;
		case MENU_CANCEL:
			finish();
			return true;		
		}
		
		return true;
	}
	
	@Override
	//http://www.anddev.org/tinytutcontextmenu_for_listview-t4019.html
	//UGH, things changed from .9 to 1.0
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) { 
		  //TODO:  IMPLEMENT CONTEXT MENU
		  default: 
		    return super.onContextItemSelected(item); 
		  }
		  
	}
	
	

}

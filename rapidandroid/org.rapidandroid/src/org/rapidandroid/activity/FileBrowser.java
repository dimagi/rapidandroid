/*
 * Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package org.rapidandroid.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.rapidandroid.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Simple text based file browser activity to search the SD card.
 * 
 * Source taken from http://www.anddev.org/android_filebrowser__v20-t101.html
 * (using version 1 code which is in this link)
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created 1/31/2009
 * 
 * 
 */
public class FileBrowser extends ListActivity {
	private enum DISPLAYMODE {
		ABSOLUTE, RELATIVE;
	}

	private final DISPLAYMODE displayMode = DISPLAYMODE.ABSOLUTE;
	private List<String> directoryEntries = new ArrayList<String>();
	private File currentDirectory = new File(Environment.getExternalStorageDirectory(), "rapidandroid/exports");

	private File mSelectedFile;

	private static final int MENU_CHOOSE = Menu.FIRST;
	private static final int MENU_CANCEL = Menu.FIRST + 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// setContentView() gets called within the next line,
		// so we do not need it here.
		// browseToRoot();
		browseTo(currentDirectory);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case MENU_CHOOSE:
				if (mSelectedFile != null) {
					chooseFile();
				}
				break;
			case MENU_CANCEL:
				finish();
				break;

		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CHOOSE, 0, "Upload");
		menu.add(0, MENU_CANCEL, 0, R.string.btn_canceladdfield);
		return true;
	}

	private void browseToRoot() {
		browseTo(new File("/"));
	}

	private void browseTo(final File aDirectory) {
		if (aDirectory.isDirectory()) {
			this.currentDirectory = aDirectory;
			fill(aDirectory.listFiles());
		}
	}

	private void chooseFile() {
		Intent ret = new Intent();
		ret.putExtra("filename", mSelectedFile.getAbsolutePath());
		setResult(FormReviewer.ACTIVITY_FILE_BROWSE, ret);
		finish();
	}

	private void fill(File[] files) {
		this.directoryEntries.clear();

		// Add the "." and the ".." == 'Up one level'
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.directoryEntries.add(".");

		if (this.currentDirectory.getParent() != null)
			this.directoryEntries.add("..");

		switch (this.displayMode) {
			case ABSOLUTE:
				for (File file : files) {
					this.directoryEntries.add(file.getPath());
				}
				break;
			case RELATIVE: // On relative Mode, we have to add the current-path
							// to
				// the beginning
				int currentPathStringLenght = this.currentDirectory.getAbsolutePath().length();
				for (File file : files) {
					this.directoryEntries.add(file.getAbsolutePath().substring(currentPathStringLenght));
				}
				break;
		}

		ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, R.layout.filebrowser_row,
																		this.directoryEntries);

		this.setListAdapter(directoryList);
	}

	/**
	 * This function browses up one level according to the field:
	 * currentDirectory
	 */
	private void upOneLevel() {
		if (this.currentDirectory.getParent() != null)
			this.browseTo(this.currentDirectory.getParentFile());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// int selectionRowID = this.getSelectedItemPosition();//or get the row?
		String selectedFileString = this.directoryEntries.get(position);
		if (selectedFileString.equals(".")) {
			// Refresh
			this.browseTo(this.currentDirectory);
			mSelectedFile = null;
		} else if (selectedFileString.equals("..")) {
			this.upOneLevel();
			mSelectedFile = null;
		} else {

			switch (this.displayMode) {
				case RELATIVE:
					mSelectedFile = new File(this.currentDirectory.getAbsolutePath()
							+ this.directoryEntries.get(position));
					break;
				case ABSOLUTE:
					mSelectedFile = new File(this.directoryEntries.get(position));
					break;
			}
		}
	}
}

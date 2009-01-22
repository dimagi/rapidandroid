package org.rapidandroid.data;

import org.rapidandroid.content.wrapper.ModelWrapper;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 *          This class helps open, create, and upgrade the database file.
 * 
 */

public class SmsDbHelper extends SQLiteOpenHelper {
	private static final String TAG = "SmsDbHelper";
	private static final String DATABASE_NAME = "rapidandroid.db";
	private static final int DATABASE_VERSION = 1;

	public SmsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// super(context, null, null, 0)
		// For eventual sd card storage:
		// SQLiteDatabase.openDatabase("/sdcard/my.db", null,
		// SQLiteDatabase.CREATE_IF_NECESSARY);
		ModelWrapper.setDbHelper(this);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String mCreateTable_Message = "CREATE TABLE \"rapidandroid_message\" ("
				+ "\"_id\" integer NOT NULL PRIMARY KEY,"
				// +
				// "\"transaction_id\" integer NULL REFERENCES \"rapidandroid_transaction\" (\"id\"),"
				+ "\"phone\" varchar(30) NULL,"
				+ "\"monitor_id\" integer NULL REFERENCES \"rapidandroid_monitor\" (\"id\"),"
				+ "\"time\" datetime NOT NULL,"
				+ "\"message\" varchar(160) NOT NULL,"
				+ "\"is_outgoing\" bool NOT NULL,"
				+ "\"is_virtual\" bool NOT NULL);";

		String mCreateTable_Monitor = "CREATE TABLE \"rapidandroid_monitor\" ("
				+ "\"_id\" integer NOT NULL PRIMARY KEY,"
				+ "\"first_name\" varchar(50) NOT NULL,"
				+ "\"last_name\" varchar(50) NOT NULL,"
				+ "\"alias\" varchar(16) NOT NULL UNIQUE,"
				+ "\"phone\" varchar(30) NOT NULL,"
				+ "\"email\" varchar(75) NOT NULL,"
				+ "\"incoming_messages\" integer unsigned NOT NULL);";

		String mCreateTable_Form = "CREATE TABLE \"rapidandroid_form\" ("
				+ "\"_id\" integer NOT NULL PRIMARY KEY,"
				+ "\"formname\" varchar(32) NOT NULL UNIQUE,"
				+ "\"prefix\" varchar(16) NOT NULL UNIQUE,"
				+ "\"description\" varchar(512) NOT NULL,"
				+ "\"parsemethod\" varchar(128) NOT NULL);";

		String mCreateTable_FieldType = "CREATE TABLE \"rapidandroid_fieldtype\" ("
				+ "\"_id\" integer NOT NULL PRIMARY KEY,"
				+ "\"name\" varchar(32) NOT NULL UNIQUE,"
				+ "\"datatype\" varchar(32) NOT NULL,"
				+ "\"regex\" varchar(1024) NOT NULL);";

		String mCreateTable_Field = "CREATE TABLE \"rapidandroid_field\" ("
				+ "\"_id\" integer NOT NULL PRIMARY KEY,"
				+ "\"form_id\" integer NOT NULL REFERENCES \"rapidandroid_form\" (\"id\"),"
				+ "\"sequence\" integer unsigned NOT NULL,"
				+ "\"name\" varchar(32) NOT NULL UNIQUE,"
				+ "\"prompt\" varchar(64) NOT NULL,"
				+ "\"fieldtype_id\" integer NOT NULL REFERENCES \"rapidandroid_fieldtype\" (\"id\"));";

		// String mCreateTable_Transaction =
		// "CREATE TABLE \"rapidandroid_transaction\" ("
		// + "\"_id\" integer NOT NULL PRIMARY KEY,"
		// + "\"identity\" integer unsigned NULL,"
		// + "\"phone\" varchar(30) NULL,"
		// +
		// "\"monitor_id\" integer NULL REFERENCES \"rapidandroid_monitor\" (\"id\"));";

		db.execSQL(mCreateTable_Message);
		db.execSQL(mCreateTable_Monitor);
		db.execSQL(mCreateTable_Form);
		db.execSQL(mCreateTable_FieldType);
		db.execSQL(mCreateTable_Field);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#getReadableDatabase()
	 */
	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub

		// for eventual usage on the SD card db storage.
		// SQLiteDatabase.openDatabase("/sdcard/my.db", null,
		// SQLiteDatabase.CREATE_IF_NECESSARY);
		//		
		return super.getReadableDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#getWritableDatabase()
	 */
	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		return super.getWritableDatabase();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		// db.execSQL("DROP TABLE IF EXISTS notes");
		onCreate(db);
	}

}
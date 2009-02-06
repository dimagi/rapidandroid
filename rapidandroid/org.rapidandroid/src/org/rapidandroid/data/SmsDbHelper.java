package org.rapidandroid.data;

import java.io.File;

import org.rapidandroid.content.translation.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * This class helps open, create, and upgrade the database file. <br>
 * By default it's hard coded to store the DB on the SD card. Thread safety and
 * closure safety are pulled straight from the parent class for db management
 * for getReadable() and getWriteable()
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 * 
 * 
 */

public class SmsDbHelper extends SQLiteOpenHelper {
	private static final String TAG = "SmsDbHelper";

	private static final String DATABASE_NAME = "rapidandroid.db";
	private static final String DATABASE_PATH_EXTERNAL = "/sdcard/rapidandroid/rapidandroid.db";
	private static final String DATABASE_PATH_LOCAL = "rapidandroid.db";

	private boolean useLocal = false;
	private String dbPathToUse = DATABASE_PATH_EXTERNAL;

	// private static final String DATABASE_NAME = "rapidandroid.db";
	//private static final int DATABASE_VERSION = 1;	//version 1:  initial version 1/22/2009
	private static final int DATABASE_VERSION = 2;		//2/6/2007, add receive_time column to message table

	// Sections lifted from the originating class SqliteOpenHelper.java
	private SQLiteDatabase mDatabase = null;
	private boolean mIsInitializing = false;

	public SmsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// super(context, null, null, 0)
		// For eventual sd card storage:
		// SQLiteDatabase.openDatabase("/sdcard/my.db", null,
		// SQLiteDatabase.CREATE_IF_NECESSARY);

		File sdcard = Environment.getExternalStorageDirectory();

		File destination = new File(sdcard, "rapidandroid");
		if (destination.mkdir()) {
			Log.d("SmsDbHelper", "Application data directory created");
		}
		if (destination.exists()) {
			useLocal = false;
			dbPathToUse = DATABASE_PATH_EXTERNAL;
		} else {
			useLocal = true;
			dbPathToUse = DATABASE_PATH_LOCAL;
		}

		// SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_NAME, null,
		// SQLiteDatabase.CREATE_IF_NECESSARY);
		// onCreate(db);
		ModelTranslator.setDbHelper(this, context);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String mCreateTable_Message = "CREATE TABLE \"rapidandroid_message\" ("
				+ "\"_id\" integer NOT NULL PRIMARY KEY,"
				// +
				// "\"transaction_id\" integer NULL REFERENCES \"rapidandroid_transaction\" (\"id\"),"
				+ "\"phone\" varchar(30) NULL,"
				+ "\"monitor_id\" integer NULL REFERENCES \"rapidandroid_monitor\" (\"id\"),"
				+ "\"time\" datetime NOT NULL," + "\"message\" varchar(160) NOT NULL,"
				+ "\"is_outgoing\" bool NOT NULL," + "\"is_virtual\" bool NOT NULL);";

		String mCreateTable_Monitor = "CREATE TABLE \"rapidandroid_monitor\" ("
				+ "\"_id\" integer NOT NULL PRIMARY KEY," + "\"first_name\" varchar(50) NOT NULL,"
				+ "\"last_name\" varchar(50) NOT NULL," + "\"alias\" varchar(16) NOT NULL UNIQUE,"
				+ "\"phone\" varchar(30) NOT NULL," + "\"email\" varchar(75) NOT NULL,"
				+ "\"incoming_messages\" integer unsigned NOT NULL," + "\"receive_reply\" bool DEFAULT '0' NOT NULL);";

		String mCreateTable_Form = "CREATE TABLE \"rapidandroid_form\" (" + "\"_id\" integer NOT NULL PRIMARY KEY,"
				+ "\"formname\" varchar(32) NOT NULL UNIQUE," + "\"prefix\" varchar(16) NOT NULL UNIQUE,"
				+ "\"description\" varchar(512) NOT NULL," + "\"parsemethod\" varchar(128) NOT NULL);";

		String mCreateTable_FieldType = "CREATE TABLE \"rapidandroid_fieldtype\" ("
				+ "\"_id\" integer NOT NULL PRIMARY KEY," + "\"name\" varchar(32) NOT NULL UNIQUE,"
				+ "\"datatype\" varchar(32) NOT NULL," + "\"regex\" varchar(1024) NOT NULL);";

		String mCreateTable_Field = "CREATE TABLE \"rapidandroid_field\" (" + "\"_id\" integer NOT NULL PRIMARY KEY,"
				+ "\"form_id\" integer NOT NULL REFERENCES \"rapidandroid_form\" (\"id\"),"
				+ "\"sequence\" integer unsigned NOT NULL," + "\"name\" varchar(32) NOT NULL UNIQUE,"
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
		if(useLocal) {
			return super.getReadableDatabase();
		}
		
		
		if (mDatabase != null && mDatabase.isOpen()) {
			return mDatabase; // The database is already open for business
		}

		if (mIsInitializing) {
			throw new IllegalStateException("getReadableDatabase called recursively");
		}

		try {
			return getWritableDatabase();
		} catch (SQLiteException e) {
			Log.e(TAG, "Couldn't open " + DATABASE_NAME + " for writing (will try read-only):", e);
		}

		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			db = SQLiteDatabase.openDatabase(dbPathToUse, null, SQLiteDatabase.OPEN_READONLY);
			if (db.getVersion() != DATABASE_VERSION) {
				throw new SQLiteException("Can't upgrade read-only database from version " + db.getVersion() + " to "
						+ DATABASE_VERSION + ": " + dbPathToUse);
			}

			onOpen(db);
			Log.w(TAG, "Opened " + DATABASE_NAME + " in read-only mode");
			mDatabase = db;
			return mDatabase;
		} finally {
			mIsInitializing = false;
			if (db != null && db != mDatabase)
				db.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#getWritableDatabase()
	 */
	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		if(useLocal) {
			return super.getWritableDatabase();
		}
		if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
			return mDatabase; // The database is already open for business
		}

		if (mIsInitializing) {
			throw new IllegalStateException("getWritableDatabase called recursively");
		}

		// If we have a read-only database open, someone could be using it
		// (though they shouldn't), which would cause a lock to be held on
		// the file, and our attempts to open the database read-write would
		// fail waiting for the file lock. To prevent that, we acquire the
		// lock on the read-only database, which shuts out other users.

		boolean success = false;
		SQLiteDatabase db = null;
		// if (mDatabase != null) mDatabase.lock(); //can't call the locks for
		// some reason. beginTransaction does lock it though
		try {
			mIsInitializing = true;
			db = SQLiteDatabase.openOrCreateDatabase(dbPathToUse, null);
			int version = db.getVersion();
			if (version != DATABASE_VERSION) {
				db.beginTransaction();
				try {
					if (version == 0) {
						onCreate(db);
					} else {
						onUpgrade(db, version, DATABASE_VERSION);
					}
					db.setVersion(DATABASE_VERSION);
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			}

			onOpen(db);
			success = true;
			return db;
		} finally {
			mIsInitializing = false;
			if (success) {
				if (mDatabase != null) {
					try {
						mDatabase.close();
					} catch (Exception e) {
					}
					// mDatabase.unlock();
				}
				mDatabase = db;
			} else {
				// if (mDatabase != null) mDatabase.unlock();
				if (db != null)
					db.close();
			}
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
//				+ ", which will destroy all old data");
		// db.execSQL("DROP TABLE IF EXISTS notes");
		//onCreate(db);
		
		if(oldVersion == 1 && newVersion == 2) {
			//version 1 to 2 introduced the receive_time for the message
			String messageAlterSql = "alter table rapidandroid_message add column receive_time datetime NULL";
			db.execSQL(messageAlterSql);
		}
		
		
		
		
	}

}
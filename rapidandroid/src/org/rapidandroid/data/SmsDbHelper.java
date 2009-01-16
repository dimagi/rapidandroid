package org.rapidandroid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
	 * This class helps open, create, and upgrade the database file.
	 */
public class SmsDbHelper extends SQLiteOpenHelper {
		private static final String TAG = "SmsDbHelper";
		private static final String DATABASE_NAME = "rapidandroid.db";
		private static final int DATABASE_VERSION = 1;

		public SmsDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String mCreateTable_Message = "CREATE TABLE \"rapidandroid_message\" ("
					+ "\"_id\" integer NOT NULL PRIMARY KEY,"
					//+ "\"transaction_id\" integer NULL REFERENCES \"rapidandroid_transaction\" (\"id\"),"
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
				+ "\"id\" integer NOT NULL PRIMARY KEY,"
				+ "\"formname\" varchar(32) NOT NULL UNIQUE,"
				+ "\"prefix\" varchar(16) NOT NULL UNIQUE,"
				+ "\"description\" varchar(512) NOT NULL,"
				+ "\"parsemethod\" varchar(128) NOT NULL);";

		String mCreateTable_FieldType = "CREATE TABLE \"rapidandroid_fieldtype\" ("
				+ "\"id\" integer NOT NULL PRIMARY KEY,"
				+ "\"name\" varchar(32) NOT NULL UNIQUE,"
				+ "\"datatype\" varchar(32) NOT NULL,"
				+ "\"regex\" varchar(1024) NOT NULL);";

		String mCreateTable_Field = "CREATE TABLE \"rapidandroid_field\" ("
				+ "\"id\" integer NOT NULL PRIMARY KEY,"
				+ "\"form_id\" integer NOT NULL REFERENCES \"rapidandroid_form\" (\"id\"),"
				+ "\"sequence\" integer unsigned NOT NULL,"
				+ "\"name\" varchar(32) NOT NULL UNIQUE,"
				+ "\"prompt\" varchar(64) NOT NULL,"
				+ "\"fieldtype_id\" integer NOT NULL REFERENCES \"rapidandroid_fieldtype\" (\"id\"));";

//			String mCreateTable_Transaction = "CREATE TABLE \"rapidandroid_transaction\" ("
//					+ "\"_id\" integer NOT NULL PRIMARY KEY,"
//					+ "\"identity\" integer unsigned NULL,"
//					+ "\"phone\" varchar(30) NULL,"
//					+ "\"monitor_id\" integer NULL REFERENCES \"rapidandroid_monitor\" (\"id\"));";

		
			db.execSQL(mCreateTable_Message);
			db.execSQL(mCreateTable_Monitor);			
			db.execSQL(mCreateTable_Form);
			db.execSQL(mCreateTable_FieldType);
			db.execSQL(mCreateTable_Field);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			//db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
package org.rapidandroid.data;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

//todo: dmyung
//implement when we are ready to write a context

public class RapidSmsContentProvider extends ContentProvider {
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */

	public static final Uri CONTENT_URI = Uri
			.parse("content://org.rapidandroid.rapidsmscontentprovider");

	private static final String TAG = "RapidSmsContentProvider";

	private SmsDbHelper mOpenHelper;

	private static final int MESSAGES = 1; 
	private static final int MESSAGES_ID = 2; 
	private static final int MONITORS = 3;
	private static final int MONITORS_ID = 4;
	private static final int MONITORS_MESSAGES_ID = 5; 
	
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		//sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(RapidAndroidData.AUTHORITY, "message", MESSAGES);		
		sUriMatcher.addURI(RapidAndroidData.AUTHORITY, "message/#",MESSAGES_ID);
		
		sUriMatcher.addURI(RapidAndroidData.AUTHORITY, "monitor", MONITORS);
		sUriMatcher.addURI(RapidAndroidData.AUTHORITY, "monitor/#",MONITORS_ID);
		sUriMatcher.addURI(RapidAndroidData.AUTHORITY, "monitor/#/messages",MONITORS_MESSAGES_ID);
				
		/*
		 * sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(RapidAndroidData.AUTHORITY, "rapid", NOTES);
        sUriMatcher.addURI(RapidAndroidData.AUTHORITY, "notes/#", NOTE_ID);

        sNotesProjectionMap = new HashMap<String, String>();
        sNotesProjectionMap.put(Notes._ID, Notes._ID);
        sNotesProjectionMap.put(Notes.TITLE, Notes.TITLE);
        sNotesProjectionMap.put(Notes.NOTE, Notes.NOTE);
        sNotesProjectionMap.put(Notes.CREATED_DATE, Notes.CREATED_DATE);
        sNotesProjectionMap.put(Notes.MODIFIED_DATE, Notes.MODIFIED_DATE);
		 * 
		 */
	}

	public RapidSmsContentProvider(Context context, String name,
			CursorFactory factory, int version) {

		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public RapidSmsContentProvider() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
        case MESSAGES:
            return RapidAndroidData.Message.CONTENT_TYPE;
        case MESSAGES_ID:
        	return RapidAndroidData.Message.CONTENT_TYPE;
        case MONITORS_ID:
        	return RapidAndroidData.Monitor.CONTENT_TYPE;
        case MONITORS_MESSAGES_ID:
        	return RapidAndroidData.Monitor.CONTENT_TYPE;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	 public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
        if (sUriMatcher.match(uri) != MESSAGES || sUriMatcher.match(uri) != MONITORS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        
        switch (sUriMatcher.match(uri)) {
		case MESSAGES:
			Long now = Long.valueOf(System.currentTimeMillis());

			// Make sure that the fields are all set
			if (values.containsKey(RapidAndroidData.Message.TIME) == false) {
				values.put(RapidAndroidData.Message.TIME, now);
			}

			if (values.containsKey(RapidAndroidData.Message.MESSAGE) == false) {
				throw new SQLException("No message");
			}

			if (values.containsKey(RapidAndroidData.Message.PHONE) == false) {
				throw new SQLException("No message");
			}

			if (values.containsKey(RapidAndroidData.Message.IS_OUTGOING) == false) {
				throw new SQLException("No direction");
			}

			if (values.containsKey(RapidAndroidData.Message.IS_VIRTUAL) == false) {
				values.put(RapidAndroidData.Message.IS_VIRTUAL, false);
			}

			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			
			
			long rowId = db.insert(RapidAndroidData.Message.TABLE,
					RapidAndroidData.Message.MESSAGE, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(
						RapidAndroidData.Message.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(noteUri, null);
				return noteUri;
			}

			throw new SQLException("Failed to insert row into " + uri);
		case MONITORS:
			
			// Make sure that the fields are all set
			if (values.containsKey(RapidAndroidData.Monitor.PHONE) == false) {
				throw new SQLException("No phone");
			}
			
			if (values.containsKey(RapidAndroidData.Monitor.ALIAS) == false) {
				values.put(RapidAndroidData.Monitor.ALIAS, values.getAsString(RapidAndroidData.Monitor.PHONE));
			}

			if (values.containsKey(RapidAndroidData.Monitor.EMAIL) == false) {
				values.put(RapidAndroidData.Monitor.EMAIL, "");
			}
			
			if (values.containsKey(RapidAndroidData.Monitor.FIRST_NAME) == false) {
				values.put(RapidAndroidData.Monitor.FIRST_NAME, "");
			}
			
			if (values.containsKey(RapidAndroidData.Monitor.LAST_NAME) == false) {
				values.put(RapidAndroidData.Monitor.LAST_NAME, "");
			}
			
			
			SQLiteDatabase dbmon = mOpenHelper.getWritableDatabase();
			long rowIdmon = dbmon.insert(RapidAndroidData.Monitor.TABLE,
					RapidAndroidData.Message.MESSAGE, values);
			if (rowIdmon > 0) {
				Uri monitorUri = ContentUris.withAppendedId(
						RapidAndroidData.Monitor.CONTENT_URI, rowIdmon);
				getContext().getContentResolver().notifyChange(monitorUri, null);
				return monitorUri;
			}

			throw new SQLException("Failed to insert row into " + uri);

		}
        return null;
        
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case MESSAGES:
			qb.setTables(RapidAndroidData.Message.TABLE);
			break;

		case MESSAGES_ID:
			qb.setTables(RapidAndroidData.Message.TABLE);
			qb.appendWhere(RapidAndroidData.Message._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case MONITORS:
			qb.setTables(RapidAndroidData.Monitor.TABLE);
			break;

		case MONITORS_ID:
			qb.setTables(RapidAndroidData.Monitor.TABLE);
			qb.appendWhere(RapidAndroidData.Monitor._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case MONITORS_MESSAGES_ID:
			qb.setTables(RapidAndroidData.Message.TABLE);
			qb.appendWhere(RapidAndroidData.Message.MONITOR + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

        // If no sort order is specified use the default
        String orderBy = sortOrder;
        
//        if (TextUtils.isEmpty(sortOrder)) {
//            orderBy = NotePad.Notes.DEFAULT_SORT_ORDER;
//        } else {
//            orderBy = sortOrder;
//        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		throw new SQLException("Update not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public boolean onCreate() {
		mOpenHelper = new SmsDbHelper(getContext());
		return true;
	}

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	private class SmsDbHelper extends SQLiteOpenHelper {
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

//			String mCreateTable_Transaction = "CREATE TABLE \"rapidandroid_transaction\" ("
//					+ "\"_id\" integer NOT NULL PRIMARY KEY,"
//					+ "\"identity\" integer unsigned NULL,"
//					+ "\"phone\" varchar(30) NULL,"
//					+ "\"monitor_id\" integer NULL REFERENCES \"rapidandroid_monitor\" (\"id\"));";

			// first, create the db
			db.execSQL(mCreateTable_Monitor);
			//db.execSQL(mCreateTable_Transaction);
			db.execSQL(mCreateTable_Message);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
	

}

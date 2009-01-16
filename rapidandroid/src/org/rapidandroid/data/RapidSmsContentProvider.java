package org.rapidandroid.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
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

	public static final Uri CONTENT_URI = Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY);

	private static final String TAG = "RapidSmsContentProvider";

	private SmsDbHelper mOpenHelper;

	private static final int MESSAGE = 1; 
	private static final int MESSAGE_ID = 2; 
	private static final int MONITOR = 3;
	private static final int MONITOR_ID = 4;
	private static final int MONITOR_MESSAGE_ID = 5;
	
	private static final int FORM = 6;
	private static final int FORM_ID = 7;
	
	private static final int FIELD = 8;
	private static final int FIELD_ID = 9;
	
	private static final int FIELDTYPE = 10;
	private static final int FIELDTYPE_ID = 11;
	
	private static final int FORMDATA_ID = 12;
	//private static final int FORMDATA_ID = 13;
	
	
	
	
	private static final UriMatcher sUriMatcher;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.Message.URI_PART, MESSAGE);		
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.Message.URI_PART+ "/#",MESSAGE_ID);
		
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.Monitor.URI_PART, MONITOR);
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.Monitor.URI_PART + "/#", MONITOR_ID);
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, "messagesbymonitor/#",MONITOR_MESSAGE_ID);
		
		
		//form field data stuffs
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.Form.URI_PART, FORM);		
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.Form.URI_PART+ "/#",FORM_ID);
		
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.Field.URI_PART, FIELD);		
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.Field.URI_PART+ "/#", FIELD_ID);
		
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.FieldType.URI_PART, FIELDTYPE);		
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.FieldType.URI_PART+ "/#",FIELDTYPE_ID);
						
		//actual form data
		sUriMatcher.addURI(RapidSmsDataDefs.AUTHORITY, RapidSmsDataDefs.FormData.URI_PART+ "/#",FORMDATA_ID);		
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
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case MESSAGE:
			return RapidSmsDataDefs.Message.CONTENT_TYPE;
		case MESSAGE_ID:
			return RapidSmsDataDefs.Message.CONTENT_ITEM_TYPE;
		case MONITOR:
			return RapidSmsDataDefs.Monitor.CONTENT_TYPE;
		case MONITOR_ID:
			return RapidSmsDataDefs.Monitor.CONTENT_ITEM_TYPE;
		case MONITOR_MESSAGE_ID:
			//this is similar to Monitor, but is filtered
			return RapidSmsDataDefs.Monitor.CONTENT_TYPE;
			
		case FORM:
			return RapidSmsDataDefs.Form.CONTENT_TYPE;
		case FORM_ID:
			return RapidSmsDataDefs.Form.CONTENT_ITEM_TYPE;
		
		case FIELD:
			return RapidSmsDataDefs.Field.CONTENT_TYPE;
		case FIELD_ID:
			return RapidSmsDataDefs.Field.CONTENT_ITEM_TYPE;

		case FIELDTYPE:
			return RapidSmsDataDefs.FieldType.CONTENT_TYPE;
		case FIELDTYPE_ID:
			return RapidSmsDataDefs.FieldType.CONTENT_ITEM_TYPE;

		case FORMDATA_ID:
			return RapidSmsDataDefs.FormData.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
			//return sUriMatcher.match(uri)+"";
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
//        if (sUriMatcher.match(uri) != MESSAGE || sUriMatcher.match(uri) != MONITOR) {
//            throw new IllegalArgumentException("Unknown URI " + uri);
//        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        
        switch (sUriMatcher.match(uri)) {
		case MESSAGE:
			return insertMessage(uri, values);
		case MONITOR:			
			return insertMonitor(uri, values);
		case FORMDATA_ID:
			throw new IllegalArgumentException("FORMDATA_ID handler not implmeneted!");
		//other stuffs not implemented for insertion yet.
			
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}        
	}

	/**
	 * @param uri
	 * @param values
	 */
	private Uri insertMessage(Uri uri, ContentValues values) {
		Long now = Long.valueOf(System.currentTimeMillis());

		// Make sure that the fields are all set
		if (values.containsKey(RapidSmsDataDefs.Message.TIME) == false) {
			values.put(RapidSmsDataDefs.Message.TIME, now);
		}

		if (values.containsKey(RapidSmsDataDefs.Message.MESSAGE) == false) {
			throw new SQLException("No message");
		}

		if (values.containsKey(RapidSmsDataDefs.Message.PHONE) == false) {
			throw new SQLException("No message");
		} else {
			ContentValues monitorValues = new ContentValues();
			monitorValues.put(RapidSmsDataDefs.Monitor.PHONE,values.getAsString(RapidSmsDataDefs.Message.PHONE));
			Uri monitorUri = insertMonitor(RapidSmsDataDefs.Monitor.CONTENT_URI,monitorValues);
			//ok, so we insert the monitor into the monitor table.
			//get the URI back and assign the foreign key into the values as part of the message insert
			values.put(RapidSmsDataDefs.Message.MONITOR, monitorUri.getPathSegments().get(1));
		}

		if (values.containsKey(RapidSmsDataDefs.Message.IS_OUTGOING) == false) {
			throw new SQLException("No direction");
		}

		if (values.containsKey(RapidSmsDataDefs.Message.IS_VIRTUAL) == false) {
			values.put(RapidSmsDataDefs.Message.IS_VIRTUAL, false);
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
		
		long rowId = db.insert(RapidSmsDataDefs.Message.TABLE,
				RapidSmsDataDefs.Message.MESSAGE, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(
					RapidSmsDataDefs.Message.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		else {
			throw new SQLException("Failed to insert row into " + uri);
		}
		
	}

	/**
	 * @param uri
	 * @param values
	 */
	private Uri insertMonitor(Uri uri, ContentValues values) {
		// Make sure that the fields are all set
		if (values.containsKey(RapidSmsDataDefs.Monitor.PHONE) == false) {
			throw new SQLException("No phone");
		}
		
		if (values.containsKey(RapidSmsDataDefs.Monitor.ALIAS) == false) {
			values.put(RapidSmsDataDefs.Monitor.ALIAS, values.getAsString(RapidSmsDataDefs.Monitor.PHONE));
		}

		if (values.containsKey(RapidSmsDataDefs.Monitor.EMAIL) == false) {
			values.put(RapidSmsDataDefs.Monitor.EMAIL, "");
		}
		
		if (values.containsKey(RapidSmsDataDefs.Monitor.FIRST_NAME) == false) {
			values.put(RapidSmsDataDefs.Monitor.FIRST_NAME, "");
		}

		if (values.containsKey(RapidSmsDataDefs.Monitor.LAST_NAME) == false) {
			values.put(RapidSmsDataDefs.Monitor.LAST_NAME, "");
		}

		if (values.containsKey(RapidSmsDataDefs.Monitor.INCOMING_MESSAGES) == false) {
			values.put(RapidSmsDataDefs.Monitor.INCOMING_MESSAGES, 0);
		}
		
		//ok, so parameters are kosher.  let's check to see if this monitor exists or not.
		System.out.println("Attempting insert of monitor: " + uri + " :: phone=" + values.getAsString(RapidSmsDataDefs.Monitor.PHONE));
		Cursor exists = query(uri,null,RapidSmsDataDefs.Monitor.PHONE + "='" + values.getAsString(RapidSmsDataDefs.Monitor.PHONE) + "'", null, null);
		System.out.println("Insert monitor query result: " + exists.getCount());
		
		if(exists.getCount() == 1) {
			//throw new SQLException("Monitor "  + values.getAsString(RapidSmsDataDefs.Monitor.PHONE) + " already exists");
			exists.moveToFirst();
//			String[] names = exists.getColumnNames();
//			for(int q = 0; q < names.length; q++) {
//				System.out.println("\tMonitorInsert: cols: " + q + "->" + names[q]);
//			}
			return ContentUris.withAppendedId(RapidSmsDataDefs.Monitor.CONTENT_URI, exists.getInt(0));
			 
		} 
		exists.close();
		

		SQLiteDatabase dbmon = mOpenHelper.getWritableDatabase();
		long rowIdmon = dbmon.insert(RapidSmsDataDefs.Monitor.TABLE,
				RapidSmsDataDefs.Monitor.EMAIL, values);
		if (rowIdmon > 0) {
			Uri monitorUri = ContentUris.withAppendedId(
					RapidSmsDataDefs.Monitor.CONTENT_URI, rowIdmon);
			getContext().getContentResolver().notifyChange(monitorUri, null);
			return monitorUri;
		} else {
			throw new SQLException("Failed to insert row into " + uri);
		}
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String table;
		String finalWhere = "";
			
		
		switch (sUriMatcher.match(uri)) {
		case MESSAGE:
			table = RapidSmsDataDefs.Message.TABLE;
			break;

		case MESSAGE_ID:
			table = RapidSmsDataDefs.Message.TABLE;
			finalWhere = RapidSmsDataDefs.Message._ID + "=" + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
			break;
		case MONITOR:
			table = RapidSmsDataDefs.Monitor.TABLE;
			break;

		case MONITOR_ID:
			table = RapidSmsDataDefs.Monitor.TABLE;
			finalWhere = RapidSmsDataDefs.Message._ID + "=" + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
			break;
		case MONITOR_MESSAGE_ID:
			table = RapidSmsDataDefs.Message.TABLE;
//			qb.appendWhere(RapidSmsDataDefs.Message.MONITOR + "="
//					+ uri.getPathSegments().get(1));
			
			finalWhere = RapidSmsDataDefs.Message.MONITOR + "=" + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		if(finalWhere == "") {
			finalWhere = where;
		}
		
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        int result = db.delete(table, finalWhere, whereArgs);

        return result;
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
		case MESSAGE:
			qb.setTables(RapidSmsDataDefs.Message.TABLE);
			break;

		case MESSAGE_ID:
			qb.setTables(RapidSmsDataDefs.Message.TABLE);
			qb.appendWhere(RapidSmsDataDefs.Message._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case MONITOR:
			qb.setTables(RapidSmsDataDefs.Monitor.TABLE);
			break;

		case MONITOR_ID:
			qb.setTables(RapidSmsDataDefs.Monitor.TABLE);
			qb.appendWhere(RapidSmsDataDefs.Monitor._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case MONITOR_MESSAGE_ID:
			qb.setTables(RapidSmsDataDefs.Message.TABLE);
			qb.appendWhere(RapidSmsDataDefs.Message.MONITOR + "="
					+ uri.getPathSegments().get(1));
			break;
		case FORM:
			throw new IllegalArgumentException(uri + " query handler not implemented.");
			//todo
		case FORM_ID:
			throw new IllegalArgumentException(uri + " query handler not implemented.");
		case FIELD:
			throw new IllegalArgumentException(uri + " query handler not implemented.");
		case FIELD_ID:
			throw new IllegalArgumentException(uri + " query handler not implemented.");
		case FIELDTYPE:
			throw new IllegalArgumentException(uri + " query handler not implemented.");
		case FIELDTYPE_ID:
			throw new IllegalArgumentException(uri + " query handler not implemented.");
		case FORMDATA_ID:
			throw new IllegalArgumentException(uri + " query handler not implemented.");
			
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
		throw new IllegalArgumentException("Update not implemented");
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

	
	

}

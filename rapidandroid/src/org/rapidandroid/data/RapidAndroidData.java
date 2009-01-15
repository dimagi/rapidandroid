/**
 * 
 */
package org.rapidandroid.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author dmyung
 * @created Jan 14, 2009
 */
public final class RapidAndroidData {
	public static final String AUTHORITY = "org.rapidandroid.rapidandroiddata";
	private RapidAndroidData() {}
	
	
	
	/**
     * Message table
     */
    public static final class Message implements BaseColumns {
//    	+ "\"id\" integer NOT NULL PRIMARY KEY,"
//		+ "\"transaction_id\" integer NULL REFERENCES \"rapidandroid_transaction\" (\"id\"),"
//		+ "\"phone\" varchar(30) NULL,"
//		+ "\"monitor_id\" integer NULL REFERENCES \"rapidandroid_monitor\" (\"id\"),"
//		+ "\"time\" datetime NOT NULL,"
//		+ "\"message\" varchar(160) NOT NULL,"
//		+ "\"is_outgoing\" bool NOT NULL,"
//		+ "\"is_virtual\" bool NOT NULL);";

    	public static final String TABLE = "rapidandroid_message";
    	
    	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/message");
    	
    	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/org.rapidandroid.message";
    	
    	public static final String PHONE = "phone";
    	public static final String MESSAGE = "message";
    	public static final String MONITOR = "monitor_id";
    	public static final String TIME = "time";    	    	
    	public static final String IS_OUTGOING = "is_outgoing";
    	public static final String IS_VIRTUAL = "is_virtual";    	    	
    }
    
    /**
     * Monitor table
     */
    public static final class Monitor implements BaseColumns {
//    	+ "\"_id\" integer NOT NULL PRIMARY KEY,"
//		+ "\"first_name\" varchar(50) NOT NULL,"
//		+ "\"last_name\" varchar(50) NOT NULL,"
//		+ "\"alias\" varchar(16) NOT NULL UNIQUE,"
//		+ "\"phone\" varchar(30) NOT NULL,"
//		+ "\"email\" varchar(75) NOT NULL,"
//		+ "\"incoming_messages\" integer unsigned NOT NULL);";
    	public static final String TABLE = "rapidandroid_monitor";
    	
    	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/org.rapidandroid.monitor";
    	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/monitor");
    	
    	public static final String LAST_NAME = "last_name";
    	public static final String FIRST_NAME = "first_name";
    	public static final String ALIAS = "alias";
    	public static final String PHONE = "phone";
    	public static final String EMAIL = "email";
    	public static final String INCOMING_MESSAGES = "incoming_messages";
        	  
    }

    /**
     * Transaction table
     */
//    public static final class Transaction implements BaseColumns {
////    	+ "\"_id\" integer NOT NULL PRIMARY KEY,"
////		+ "\"identity\" integer unsigned NULL,"
////		+ "\"phone\" varchar(30) NULL,"
////		+ "\"monitor_id\" integer NULL REFERENCES \"rapidandroid_monitor\" (\"id\"));";
//    	public static final String TABLE = "rapidandroid_transaction";
//    	public static final String LAST_NAME = "identity";    	
//    	public static final String PHONE = "phone";
//    	public static final String MONITOR_ID = "monitor_id";
//    	
//
//    }
	

}

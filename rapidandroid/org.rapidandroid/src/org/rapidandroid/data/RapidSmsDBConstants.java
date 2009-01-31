package org.rapidandroid.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 14, 2009
 * 
 *          Helper constants for table and querying for the content provider and
 *          Sql helper
 */

public final class RapidSmsDBConstants {
	public static final String AUTHORITY = "org.rapidandroid.provider.RapidSms";

	private RapidSmsDBConstants() {
	}

	/**
	 * Message table
	 */
	public static final class Message implements BaseColumns {

		public static final String TABLE = "rapidandroid_message";

		public static final String URI_PART = "message";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + URI_PART);
		public static final String CONTENT_URI_STRING = "content://"
			+ AUTHORITY + "/" + URI_PART + "/";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/org.rapidandroid.data.message";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/org.rapidandroid.data.message";

		/**
		 * 
		 * Phone field is a helper for inserting to the content provider.  columnm is there as legacy, but shouldn't be used.
		 */
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

		// Structural stuffs
		public static final String TABLE = "rapidandroid_monitor";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/org.rapidandroid.data.monitor";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/org.rapidandroid.data.monitor";

		public static final String URI_PART = "mMonitorString";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + URI_PART);

		public static final Uri MESSAGE_BY_MONITOR_URI = Uri.parse("content://"
				+ AUTHORITY + "/messagesbymonitor");

		// columns
		public static final String LAST_NAME = "last_name";
		public static final String FIRST_NAME = "first_name";
		public static final String ALIAS = "alias";
		public static final String PHONE = "phone";
		public static final String EMAIL = "email";
		public static final String INCOMING_MESSAGES = "incoming_messages";

	}

	public static final class Form implements BaseColumns {
		// Structural stuffs
		public static final String TABLE = "rapidandroid_form";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/org.rapidandroid.data.form";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/org.rapidandroid.data.form";

		public static final String URI_PART = "form";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + URI_PART);
		public static final String CONTENT_URI_STRING = "content://"
				+ AUTHORITY + "/" + URI_PART + "/";

		// columns
		public static final String FORMNAME = "formname";
		public static final String PREFIX = "prefix";
		public static final String DESCRIPTION = "description";
		public static final String PARSEMETHOD = "parsemethod";

	}

	public static final class Field implements BaseColumns {
		// Structural stuffs
		public static final String TABLE = "rapidandroid_field";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/org.rapidandroid.data.field";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/org.rapidandroid.data.field";

		public static final String URI_PART = "field";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + URI_PART);
		public static final String CONTENT_URI_STRING = "content://"
				+ AUTHORITY + "/" + URI_PART + "/";

		// columns
		public static final String FORM = "form_id";
		public static final String SEQUENCE = "sequence";
		public static final String NAME = "name";
		public static final String PROMPT = "prompt";
		public static final String FIELDTYPE = "fieldtype_id";
	}

	public static final class FieldType implements BaseColumns {
		// Structural stuffs
		public static final String TABLE = "rapidandroid_fieldtype";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/org.rapidandroid.data.fieldtype";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/org.rapidandroid.data.fieldtype";

		public static final String URI_PART = "fieldtype";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + URI_PART);
		public static final String CONTENT_URI_STRING = "content://"
				+ AUTHORITY + "/" + URI_PART + "/";

		// columns
		public static final String NAME = "name";
		public static final String DATATYPE = "datatype";
		public static final String REGEX = "regex";

	}

	public static final class FormData implements BaseColumns {
		// Structural stuffs
		public static final String TABLE_PREFIX = "formdata_"; // and put the
																// formprefix
																// there!

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/org.rapidandroid.data.formdata";
		// public static final String CONTENT_ITEM_TYPE =
		// "vnd.android.cursor.item/org.rapidandroid.data.formdata";

		public static final String URI_PART = "formdata";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + URI_PART);// hrmm, this is tricky
		public static final String CONTENT_URI_PREFIX = "content://"
				+ AUTHORITY + "/" + URI_PART + "/"; // needs to add the id

		// columns
		public static final String MESSAGE = "message_id";
		// since these tables are dynamically generated, the column prefix is
		// affixed to all columns generated by the form definition. The suffix
		// is the Field
		public static final String COLUMN_PREFIX = "col_";

	}

}

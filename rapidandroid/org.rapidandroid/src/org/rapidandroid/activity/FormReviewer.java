/**
 * 
 */
package org.rapidandroid.activity;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.rapidandroid.ActivityConstants;
import org.rapidandroid.R;
import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.content.translation.ParsedDataTranslator;
import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.data.controller.ParsedDataReporter;
import org.rapidandroid.view.adapter.FieldViewAdapter;
import org.rapidsms.java.core.Constants;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.model.Monitor;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.service.ParsingService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for reviewing a form and doing form specific activities.
 * Namely, CSV report output, HTTP upload, and a "hint" for how to compose the form in an SMS message using. 
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 */

public class FormReviewer extends Activity {

	public class CallParams {
		public static final String REVIEW_FORM = "review_form";
	}

	private static final int MENU_DONE = Menu.FIRST;
	private static final int MENU_FORMAT = Menu.FIRST + 1;
	private static final int MENU_DUMP_CSV = Menu.FIRST + 2;
	private static final int MENU_HTTP_UPLOAD = Menu.FIRST + 3;
	private static final int MENU_INJECT_DEBUG = Menu.FIRST + 4;

	public static final int ACTIVITY_FILE_BROWSE = 0;

	boolean success = false;

	private Form mForm;
	final Handler mDebugHandler = new Handler();

	final Runnable mCsvSaveCompleted = new Runnable() {
		public void run() {
			alertCSVStatus();
		}
	};

	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			updateResultsInUi();
		}
	};

	final Runnable mFinishUpload = new Runnable() {
		public void run() {
			alertUploadStatus();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("RapidAndroid :: Review Form");
		setContentView(R.layout.form_edit);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			if (!extras.containsKey(CallParams.REVIEW_FORM)) {
				throw new IllegalArgumentException(
						"Error, activity was called without a Form ID to review.");
			}
			int formID = extras.getInt(CallParams.REVIEW_FORM);
			mForm = ModelTranslator.getFormById(formID);

			TextView txv_formname = (TextView) findViewById(R.id.txv_formname);
			TextView txv_prefix = (TextView) findViewById(R.id.txv_formprefix);
			TextView txv_description = (TextView) findViewById(R.id.txv_description);

			ListView lsv_fields = (ListView) findViewById(R.id.lsv_fields);

			txv_formname.setText(mForm.getFormName());
			txv_prefix.setText(mForm.getPrefix());
			txv_description.setText(mForm.getDescription());

			int len = mForm.getFields().length;
			
			
//			lsv_fields.setAdapter(new ArrayAdapter<String>(this,
//					android.R.layout.simple_list_item_1, fields));
			lsv_fields.setAdapter(new FieldViewAdapter(this,mForm.getFields()));

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_DONE, 0, R.string.formreview_menu_done).setIcon(
				android.R.drawable.ic_menu_revert);

		menu.add(0, MENU_FORMAT, 0, R.string.formreview_menu_format).setIcon(
				android.R.drawable.ic_menu_info_details);

		menu.add(0, MENU_DUMP_CSV, 0, R.string.formreview_dump_csv).setIcon(
				android.R.drawable.ic_menu_save);

		menu.add(0, MENU_HTTP_UPLOAD, 0, R.string.formreview_upload_csv)
				.setIcon(android.R.drawable.ic_menu_upload);

		menu.add(0, MENU_INJECT_DEBUG, 0, "Generate Data").setIcon(
				android.R.drawable.ic_menu_manage);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_DONE:
			finish();
			return true;
		case MENU_FORMAT:
			// Intent mIntent = new Intent();
			// mIntent.putExtras(bundle);
			// setResult(RESULT_OK, mIntent)

			try {
				// this is because the randomization doesn't get instantiated
				// for some weird reason
				// unless we blow away the dialog
				removeDialog(0);
			} catch (Exception ex) {
			}
			showDialog(0);
			return true;
		case MENU_DUMP_CSV:
			if(ParsedDataReporter.getOldestMessageDate(this, mForm).equals(Constants.NULLDATE)) {
				Builder noDateDialog = new AlertDialog.Builder(this);
				noDateDialog.setPositiveButton("Ok", null);
				noDateDialog.setTitle("Alert");
				noDateDialog.setMessage("This form has no messages or data to output");
				noDateDialog.show();
				return true;
			}
			
			outputCSV();
			break;
		case MENU_HTTP_UPLOAD:
			chooseFile();
			break;
		case MENU_INJECT_DEBUG:
			injectMessages();
			break;

		}
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		String title = "Sample submission";

		if (mForm == null) {
			return null;
		}

		StringBuilder sb = generateRandomMessage();

		return new AlertDialog.Builder(FormReviewer.this).setTitle(title).setMessage(sb.toString().trim())
															.setPositiveButton("OK", null).create();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Bundle extras = null;
		super.onActivityResult(requestCode, resultCode, intent);
		if (intent != null) {
			extras = intent.getExtras();

			switch (requestCode) {
			case ACTIVITY_FILE_BROWSE:
				// get the filename
				String filename = extras.getString("filename");
				uploadFile(filename);
				break;
			default:
				break;
			}
		}

	}

	private void alertUploadStatus() {
		if (success) {
			Toast.makeText(getApplicationContext(), "File upload successful",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), "File upload failed",
					Toast.LENGTH_LONG).show();
		}
	}

	private void alertCSVStatus() {

		Toast.makeText(getApplicationContext(), "CSV Save Complete",
				Toast.LENGTH_LONG).show();

	}

	private void updateResultsInUi() {
		Toast.makeText(getApplicationContext(), "Data injection completed",
				Toast.LENGTH_LONG).show();

	}

	private void chooseFile() {
		// spawn the activity for file browsing
		Intent i;
		i = new Intent(this, FileBrowser.class);
		startActivityForResult(i, ACTIVITY_FILE_BROWSE);
		// on activity return do the upload
	}

	private void uploadFile(final String filename) {
		Toast.makeText(getApplicationContext(), "File upload begun",
				Toast.LENGTH_LONG).show();
		Thread t = new Thread() {
			public void run() {
				try {
					DefaultHttpClient httpclient = new DefaultHttpClient();

					File f = new File(filename);

					HttpPost httpost = new HttpPost(
							"http://192.168.7.127:8160/upload/upload");
					MultipartEntity entity = new MultipartEntity();
					entity.addPart("myIdentifier", new StringBody("somevalue"));
					entity.addPart("myFile", new FileBody(f));
					httpost.setEntity(entity);

					HttpResponse response;

					// Post, check and show the result (not really spectacular,
					// but works):
					response = httpclient.execute(httpost);

					Log.d("httpPost", "Login form get: "
							+ response.getStatusLine());

					if (entity != null) {
						entity.consumeContent();
					}

					success = true;
				} catch (Exception ex) {
					Log.d("FormReviewer", "Upload failed: " + ex.getMessage()
							+ " Stacktrace: " + ex.getStackTrace());
					success = false;
				} finally {
					mDebugHandler.post(mFinishUpload);
				}
			}
		};
		t.start();
	}

	/**
	 * 
	 */
	private void outputCSV() {
		Toast.makeText(getApplicationContext(), "CSV output job has begun",
				Toast.LENGTH_LONG).show();
		// Fire off a thread to do some work that we shouldn't do directly in
		// the UI thread
		Thread t = new Thread() {
			public void run() {
				Calendar now = Calendar.getInstance();
				Calendar then = Calendar.getInstance();								
				then.set(Calendar.YEAR, 1990);
				ParsedDataReporter.exportFormDataToCSV(getBaseContext(), mForm, then, now);
				mDebugHandler.post(mCsvSaveCompleted);
				
			}
		};
		t.start();
	}

	/**
	 * 
	 */
	private void injectMessages() {
		Toast.makeText(getApplicationContext(), "Debug data injection started",
				Toast.LENGTH_LONG).show();
		// Fire off a thread to do some work that we shouldn't do directly in
		// the UI thread
		Thread t = new Thread() {
			public void run() {
				doInjection();
				mDebugHandler.post(mUpdateResults);
			}
		};
		t.start();
	}

	private void doInjection() {
		Random r = new Random();

		//Debug.startMethodTracing("injection");
		for (int i = 0; i < 100; i++) {

			// first, let's get the
			String token = phones[r.nextInt(phones.length)];//Long.toString(Math.abs(r.nextLong()), 36);
			Monitor monitor = MessageTranslator.GetMonitorAndInsertIfNew(this, token);
			
			Uri writeMessageUri = RapidSmsDBConstants.Message.CONTENT_URI;

			StringBuilder sb = this.generateRandomMessage();
			ContentValues messageValues = new ContentValues();
			messageValues.put(RapidSmsDBConstants.Message.MESSAGE, sb.toString());
			messageValues.put(RapidSmsDBConstants.Message.MONITOR, monitor.getID());

			Date now = getRandomDate();

			messageValues.put(RapidSmsDBConstants.Message.TIME,
					Message.SQLDateFormatter.format(now));
			messageValues.put(RapidSmsDBConstants.Message.IS_OUTGOING, false);

			Uri msgUri = null;

			msgUri = getContentResolver()
					.insert(writeMessageUri, messageValues);

			Vector<IParseResult> results = ParsingService.ParseMessage(mForm,sb.toString());
			ParsedDataTranslator.InsertFormData(this, mForm, Integer.valueOf(
					msgUri.getPathSegments().get(1)).intValue(), results);
		}
		
		Debug.stopMethodTracing();

	}

	private Date getRandomDate() {
		Calendar cdr = Calendar.getInstance();
		//cdr.set(1999, 1, 1);
		cdr.set(Calendar.MONTH,cdr.get(Calendar.MONTH) -3);
		cdr.set(Calendar.HOUR_OF_DAY, 0);
		cdr.set(Calendar.MINUTE, 0);
		cdr.set(Calendar.SECOND, 0);
		long val1 = cdr.getTimeInMillis();

		cdr = Calendar.getInstance();
		cdr.set(Calendar.HOUR_OF_DAY, 23);
		cdr.set(Calendar.MINUTE, 59);
		cdr.set(Calendar.SECOND, 0);
		long val2 = cdr.getTimeInMillis();

		Random r = new Random();
		long randomTS = (long) (r.nextDouble() * (val2 - val1)) + val1;
		Date d = new Date(randomTS);
		return d;
	}

	private static String[] bools = new String[] { "t", "f", "true", "false",
			"yes", "no", "y", "n" };
	private static String[] heights = new String[] { "cm", "m", "meters",
			"meter" };
	private static String[] lengths = new String[] { "cm", "m" };
	private static String[] weights = new String[] { "kg", "kilos", "kilo", "kg" };
	private static String[] words = new String[] { "bos", "nyc", "jfk", "lax",
			"lun", "lhr", "asvasd", "alksjwlejrwer", "bshdkghk", "akhsdwer",
			"tiwowuy", "xvcxbxkhcvb" };
	private static String[] phones = new String[] {"5558675309","6175803100", "2128246918", "2123267768", "6175803103"};
	
	private static String[] floats = new String[] { "0.24", "0.54", "1.5","50%", "25 pct","33 %", "15pct", "2/3",  "3:2"};
	
	Random r = new Random();

	/**
	 * @return
	 */
	private StringBuilder generateRandomMessage() {
		StringBuilder sb = new StringBuilder();

		sb.append(mForm.getPrefix() + " ");

		Field[] fields = mForm.getFields();
		int len = fields.length;

		for (int i = 0; i < len; i++) {
			Field field = fields[i];

			String type = field.getFieldType().getReadableName();

			if (type.toLowerCase().equals("word")) {
				sb.append(words[r.nextInt(words.length)]);
			} else if (type.toLowerCase().equals("number")) {
				sb.append(r.nextInt(1000));
			} else if (type.equals("Height")) {
				sb.append(r.nextInt(200) + " "
						+ heights[r.nextInt(heights.length)]);
			} else if (type.toLowerCase().equals("boolean") || type.toLowerCase().equals("yes/no")) {
				sb.append(bools[r.nextInt(bools.length)]);
			} else if (type.toLowerCase().equals("length")) {
				sb.append(r.nextInt(200) + " "
						+ lengths[r.nextInt(lengths.length)]);
			} else if (type.toLowerCase().equals("ratio")) {
				
				sb.append(floats[r.nextInt(floats.length)]);
			} else if (type.toLowerCase().equals("weight")) {
				sb.append(r.nextInt(150) + " "
						+ weights[r.nextInt(weights.length)]);
			}
			sb.append(" ");

		}
		return sb;
	}
}

package org.rapidandroid.activity;

import java.util.Calendar;
import java.util.Date;

import org.rapidandroid.R;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.model.SimpleFieldType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * An activity that lets you choose a date range with two sliders.
 * 
 * To call the activity, you must supply a start date (recent) and end date (point in the past)
 * To set as the boundaries of the daterange you want to pick.
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 30, 2009
 * 
 */
public class DateRange extends Activity {

	
	public class CallParams {
		/**
		 * How far back does this dataset go back in time.
		 */
		public static final String ACTIVITY_ARG_ENDDATE = "enddate";
	
	}
	
	public class ResultParams {
		/**
		 * The more recent time returned
		 */
		public static final String RESULT_START_DATE = "startdate";
		/**
		 * How far into the past to look back.  
		 */
		public static final String RESULT_END_DATE = "enddate";
	}
	
	public static final int ACTIVITYRESULT_DATERANGE = 1970;
	

	private Date mStartNow;
	private Date mAbsoluteEndDate;
	
	private SeekBar seekEnd;
	private SeekBar seekStart;

	private TextView txvStartDate;
	private TextView txvEndDate;

	private static final int MENU_DONE = Menu.FIRST;
	private static final int MENU_CANCEL = Menu.FIRST + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("Select Date Range");
		setContentView(R.layout.date_range);
		setEventListeners();
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			if(!extras.containsKey(CallParams.ACTIVITY_ARG_ENDDATE)) {
				throw new IllegalArgumentException("This activity must be called with an appropriate enddate");
			}
			String datestring = extras.getString(CallParams.ACTIVITY_ARG_ENDDATE);
			try {
				mStartNow = new Date();
				mAbsoluteEndDate = Message.SQLDateFormatter.parse(datestring);

			} catch (Exception ex) {

			}
		} 
		setSliders();
	}

	private void setSliders() {

	}

	

	private void setEventListeners() {
		seekEnd = (SeekBar) findViewById(R.id.range_endseek);

		seekEnd.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromTouch) {
				updateDisplayFromSliders();

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				updateDisplayFromSliders();

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				updateDisplayFromSliders();
			}

		});

		seekStart = (SeekBar) findViewById(R.id.range_startseek);
		seekStart.setSecondaryProgress(seekEnd.getProgress());
		seekStart.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromTouch) {
				updateDisplayFromSliders();

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				updateDisplayFromSliders();
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				updateDisplayFromSliders();
			}

		});

		Button btnOneMonth = (Button) findViewById(R.id.range_btn_last_month);
		btnOneMonth.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar delta = Calendar.getInstance();
				delta.setTimeInMillis(mStartNow.getTime());
				delta.set(Calendar.DATE, delta.get(Calendar.DATE) - 30);
				updateDisplayFromDate(delta);
			}

		});
		Button btnOneWeek = (Button) findViewById(R.id.range_btn_lastweek);
		btnOneWeek.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Calendar delta = Calendar.getInstance();
				delta.setTimeInMillis(mStartNow.getTime());
				delta.set(Calendar.DATE, delta.get(Calendar.DATE) - 7);
				updateDisplayFromDate(delta);

			}

		});
		Button btnOneDay = (Button) findViewById(R.id.range_btn_lastday);
		btnOneDay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Calendar delta = Calendar.getInstance();
				delta.setTimeInMillis(mStartNow.getTime());
				delta.set(Calendar.DATE, delta.get(Calendar.DATE) - 1);
				updateDisplayFromDate(delta);
			}
		});
	}

	private void updateDisplayFromDate(Calendar delta) {
		long tickDelta = mStartNow.getTime() - mAbsoluteEndDate.getTime();
		long increment = tickDelta / 100;
		long slice = mStartNow.getTime() - delta.getTimeInMillis();

		if (increment > 0) {
			int ticks = (int) (slice / increment);
			seekStart.setProgress(0);
			seekEnd.setProgress(ticks);
			seekStart.setSecondaryProgress(seekEnd.getProgress());			
			
			txvStartDate = (TextView) findViewById(R.id.txv_startdate);
			txvStartDate.setText(Message.DisplayDateTimeFormat.format(mStartNow
					.getTime()
					- (seekStart.getProgress() * increment)));

			txvEndDate = (TextView) findViewById(R.id.txv_enddate);
			txvEndDate.setText(Message.DisplayDateTimeFormat.format(delta.getTimeInMillis()));
			
		}
	}

	private void updateDisplayFromSliders() {
		// Let's make sure the displays are ok
		if (seekEnd.getProgress() < seekStart.getProgress()) {
			seekEnd.setProgress(seekStart.getProgress());
		}
		seekStart.setSecondaryProgress(seekEnd.getProgress());
		long tickDelta = mStartNow.getTime() - mAbsoluteEndDate.getTime();
		long increment = tickDelta / 100;
		
		if (increment > 0) {
			txvStartDate = (TextView) findViewById(R.id.txv_startdate);
			txvStartDate.setText(Message.DisplayDateTimeFormat.format(mStartNow
					.getTime()
					- (seekStart.getProgress() * increment)));

			txvEndDate = (TextView) findViewById(R.id.txv_enddate);
			txvEndDate.setText(Message.DisplayDateTimeFormat.format(mStartNow
					.getTime()
					- (seekEnd.getProgress() * increment)));
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_DONE, 0, R.string.formreview_menu_done).setIcon(
				android.R.drawable.ic_menu_edit);
		menu.add(0, MENU_CANCEL, 0, R.string.formeditor_menu_cancel).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_DONE:
			Intent ret = new Intent();
			
			txvStartDate = (TextView) findViewById(R.id.txv_startdate);
			

			txvEndDate = (TextView) findViewById(R.id.txv_enddate);
			
			ret.putExtra(ResultParams.RESULT_START_DATE, txvStartDate.getText().toString());
			ret.putExtra(ResultParams.RESULT_END_DATE, txvEndDate.getText().toString());
			setResult(ACTIVITYRESULT_DATERANGE, ret);
			finish();
			return true;
		case MENU_CANCEL:
			finish();
			return true;
		}
		return true;
	}

}

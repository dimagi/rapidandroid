package org.rapidandroid.activity;

import java.util.Calendar;
import java.util.Date;

import org.rapidandroid.R;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.model.SimpleFieldType;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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
		public static final String ACTIVITY_ARG_STARTDATE = "startdate";
	
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
	

	private Date mStartDate;
	private Date mEndDate;
	
	private TextView txvStartDate;
	private TextView txvEndDate;

	private static final int MENU_DONE = Menu.FIRST;
	private static final int MENU_CANCEL = Menu.FIRST + 1;
	

    private int mStartYear;
    private int mStartMonth;
    private int mStartDay;

    private int mEndYear;
    private int mEndMonth;
    private int mEndDay;
    
    int mStartBeginningYear;
    int mStartBeginningMonth;
    int mStartBeginningDay;
	
	static final int DATE_DIALOG_START_ID = 0;
	static final int DATE_DIALOG_END_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("Select Date Range");
		setContentView(R.layout.date_range);
		setEventListeners();
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			if(!extras.containsKey(CallParams.ACTIVITY_ARG_STARTDATE)) {
				throw new IllegalArgumentException("This activity must be called with an appropriate startdate in the past.");
			}
			
			mStartDate = new Date(extras.getLong(CallParams.ACTIVITY_ARG_STARTDATE));
			mEndDate = new Date();	//now
			
		} 
		
		txvStartDate = (TextView)findViewById(R.id.txv_startdate);
		txvEndDate = (TextView)findViewById(R.id.txv_enddate);
		Calendar s = Calendar.getInstance();
		s.setTime(mStartDate);
		Calendar e = Calendar.getInstance();
		e.setTime(mEndDate);
		//e.set(Calendar.DATE, e.get(Calendar.DATE)+1);	//we want to increment this by one day to avoid against running up against boundary conditions on recent data.
		
		mStartBeginningYear = s.get(Calendar.YEAR);
		mStartBeginningMonth = s.get(Calendar.MONTH);
		mStartBeginningDay = s.get(Calendar.DAY_OF_MONTH);		
		
        setUpdateCalendar(s,e);
	}
	
	private void setUpdateCalendar(Calendar s, Calendar e) {		
		mStartYear = s.get(Calendar.YEAR);
		mStartMonth = s.get(Calendar.MONTH);
		mStartDay = s.get(Calendar.DAY_OF_MONTH);		
		
		mEndYear = e.get(Calendar.YEAR);
		mEndMonth = e.get(Calendar.MONTH);
		mEndDay = e.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
	}
	
	
	private void updateDisplay() {
		txvStartDate.setText(
	            new StringBuilder()
	                    // Month is 0 based so add 1
	                    .append(mStartMonth + 1).append("-")
	                    .append(mStartDay).append("-")
	                    .append(mStartYear));
		
		txvEndDate.setText(
	            new StringBuilder()
	                    // Month is 0 based so add 1
	                    .append(mEndMonth + 1).append("-")
	                    .append(mEndDay).append("-")
	                    .append(mEndYear));
	}
	
	private DatePickerDialog.OnDateSetListener mStartDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                mStartYear = year;
                mStartMonth = monthOfYear;
                mStartDay = dayOfMonth;
                updateDisplay();
            }
        };

        private DatePickerDialog.OnDateSetListener mEndDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                    mEndYear = year;
                    mEndMonth = monthOfYear;
                    mEndDay = dayOfMonth;
                    updateDisplay();
                }
            };

        
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {           
            case DATE_DIALOG_START_ID:
                ((DatePickerDialog) dialog).updateDate(mStartYear, mStartMonth, mStartDay);
                break;
            case DATE_DIALOG_END_ID:
                ((DatePickerDialog) dialog).updateDate(mEndYear, mEndMonth, mEndDay);
                break;
        }
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_START_ID:
			return new DatePickerDialog(this, mStartDateSetListener,
					mStartYear, mStartMonth, mStartDay);
		case DATE_DIALOG_END_ID:
			return new DatePickerDialog(this, mEndDateSetListener, mEndYear,
					mEndMonth, mEndDay);
		}
		return null;
    }

	

	private void setEventListeners() {
		Button btnStartDate = (Button) findViewById(R.id.range_btn_startdate);
		btnStartDate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				  showDialog(DATE_DIALOG_START_ID);
			}

		});
		
		
		Button btnEndDate = (Button) findViewById(R.id.range_btn_enddate);
		btnEndDate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(DATE_DIALOG_END_ID);
			}

		});

		Button btnOneMonth = (Button) findViewById(R.id.range_btn_last_month);
		btnOneMonth.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar newEnd = Calendar.getInstance();
				Calendar newStart = Calendar.getInstance(); 
				newStart.set(Calendar.DATE, newEnd.get(Calendar.DATE) - 30);
				setUpdateCalendar(newStart,newEnd);
				
			}

		});
		Button btnOneWeek = (Button) findViewById(R.id.range_btn_lastweek);
		btnOneWeek.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Calendar newEnd = Calendar.getInstance();
				Calendar newStart = Calendar.getInstance(); 
				newStart.set(Calendar.DATE, newEnd.get(Calendar.DATE) - 7);
				setUpdateCalendar(newStart,newEnd);

			}

		});
		Button btnOneDay = (Button) findViewById(R.id.range_btn_lastday);
		btnOneDay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Calendar newEnd = Calendar.getInstance();
				Calendar newStart = Calendar.getInstance(); 
				newStart.set(Calendar.DATE, newEnd.get(Calendar.DATE) - 1);
				setUpdateCalendar(newStart,newEnd);
			}
		});
		
		Button btnThreeMonth = (Button) findViewById(R.id.range_btn_last_threemonth);
		btnThreeMonth.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Calendar newEnd = Calendar.getInstance();
				Calendar newStart = Calendar.getInstance(); 
				newStart.set(Calendar.MONTH, newEnd.get(Calendar.MONTH) - 3);
				setUpdateCalendar(newStart,newEnd);
			}
		});
		
		Button btnAll = (Button) findViewById(R.id.range_btn_entire_range);
		btnAll.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Calendar newEnd = Calendar.getInstance();
				Calendar newStart = Calendar.getInstance();
				newStart.set(Calendar.YEAR, mStartBeginningYear);
				newStart.set(Calendar.MONTH, mStartBeginningMonth);
				newStart.set(Calendar.DATE, mStartBeginningDay);
				setUpdateCalendar(newStart,newEnd);
			}
		});
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
			Calendar finalStart = Calendar.getInstance();
			finalStart.set(mStartYear, mStartMonth, mStartDay,0,0);
			
			Calendar finalEnd= Calendar.getInstance();
			finalEnd.set(mEndYear, mEndMonth, mEndDay,23,59);
			
			ret.putExtra(ResultParams.RESULT_START_DATE, finalStart.getTime().getTime());
			ret.putExtra(ResultParams.RESULT_END_DATE, finalEnd.getTime().getTime());
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

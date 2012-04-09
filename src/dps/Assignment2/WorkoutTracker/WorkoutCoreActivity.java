package dps.Assignment2.WorkoutTracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class WorkoutCoreActivity extends Activity {
	private int mYear;
	private int mMonth;
	private int mDay;
	private TextView workoutDateText;
	private Button changeDateBtn;
	static final int DATE_DIALOG_ID = 0;

	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_core);
        
        // Set Workout Date Text to today as default
        workoutDateText = (TextView)findViewById(R.id.workoutDateText);
        Time now = new Time(Time.getCurrentTimezone());
        now.setToNow();
        mYear = now.year;
        mMonth = now.month + 1;
        mDay = now.monthDay;
        workoutDateText.setText(mMonth + "/" + mDay + "/" + mYear);
        
        // Add listener to change date button
        changeDateBtn = (Button)findViewById(R.id.changeDateBtn);
        changeDateBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
        });
      
        // Populate spinner
        Spinner exerciseSpinner = (Spinner) findViewById(R.id.spinner_core);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.exercises_array_core, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);
        
        /*
        // Populate spinner
        Spinner weightSpinner = (Spinner) findViewById(R.id.spinner_weight);
        adapter = ArrayAdapter.createFromResource(
                this, R.array.exercises_array_chest, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(adapter);
        
        // Populate spinner
        Spinner repsSpinner = (Spinner) findViewById(R.id.spinner_reps);
        adapter = ArrayAdapter.createFromResource(
                this, R.array.exercises_array_chest, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repsSpinner.setAdapter(adapter);*/

    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
	    new DatePickerDialog.OnDateSetListener() {
	
	        public void onDateSet(DatePicker view, int year, 
	                              int monthOfYear, int dayOfMonth) {
	            mYear = year;
	            mMonth = monthOfYear;
	            mDay = dayOfMonth;
	            workoutDateText.setText(mMonth + "/" + mDay + "/" + mYear);
	        }
	    };
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
	
}

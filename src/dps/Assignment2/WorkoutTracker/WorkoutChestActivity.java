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
import android.widget.Toast;

public class WorkoutChestActivity extends Activity {
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
        setContentView(R.layout.workout_chest);
        
        // Set Workout Date Text to today as default
        workoutDateText = (TextView)findViewById(R.id.workoutDateText);
        Time now = new Time(Time.getCurrentTimezone());
        now.setToNow();
        mYear = now.year;
        mMonth = now.month + 1;
        mDay = now.monthDay;
        
        if (WorkoutStartTabWidget.selectedYear == -1) {
	        WorkoutStartTabWidget.selectedYear = mYear;
	        WorkoutStartTabWidget.selectedMonth = mMonth;
	        WorkoutStartTabWidget.selectedDay = mDay;
        }
        
        workoutDateText.setText(WorkoutStartTabWidget.selectedMonth + "/" + WorkoutStartTabWidget.selectedDay + "/" + WorkoutStartTabWidget.selectedYear);
        
        if (getWorkoutOn(WorkoutStartTabWidget.selectedYear,WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay) == null) {
        	WorkoutStartTabWidget.currentWorkout = createWorkoutOn(mYear, mMonth, mDay);
        }
        else {
        	WorkoutStartTabWidget.currentWorkout = getWorkoutOn(WorkoutStartTabWidget.selectedYear, WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay);
        }
        
        // Add listener to change date button
        changeDateBtn = (Button)findViewById(R.id.changeDateBtn);
        changeDateBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
        });
      
        // Populate spinner
        Spinner exerciseSpinner = (Spinner) findViewById(R.id.spinner_chest);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.exercises_array_chest, android.R.layout.simple_spinner_item);
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
    
    public void onResume() {
    	super.onResume();
    	if (getWorkoutOn(WorkoutStartTabWidget.selectedYear,WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay) == null) {
        	WorkoutStartTabWidget.currentWorkout = createWorkoutOn(mYear, mMonth, mDay);
        }
        else {
        	WorkoutStartTabWidget.currentWorkout = getWorkoutOn(WorkoutStartTabWidget.selectedYear, WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay);
        }
    	workoutDateText.setText(WorkoutStartTabWidget.selectedMonth + "/" + WorkoutStartTabWidget.selectedDay + "/" + WorkoutStartTabWidget.selectedYear);
    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
	    new DatePickerDialog.OnDateSetListener() {
	
	        public void onDateSet(DatePicker view, int year, 
	                              int monthOfYear, int dayOfMonth) {
	        	WorkoutStartTabWidget.selectedYear = year;
	            WorkoutStartTabWidget.selectedMonth = monthOfYear;
	            WorkoutStartTabWidget.selectedDay = dayOfMonth;
	            workoutDateText.setText(WorkoutStartTabWidget.selectedMonth + "/" + WorkoutStartTabWidget.selectedDay + "/" + WorkoutStartTabWidget.selectedYear);
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
	
    private Workout getWorkoutOn(int year, int month, int day) {
    	Workout workout;
    	WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutChestActivity.this);
    	workout = handler.getWorkout(year, month, day);
    	if (workout != null) {
    		Toast.makeText(this, Integer.toString(workout.getID()), Toast.LENGTH_LONG).show();
    	}
    	else {
    		Toast.makeText(this, "Not found Get", Toast.LENGTH_LONG).show();
    	}
    	handler.close();
    	return workout;
    }
    
    private Workout createWorkoutOn(int year, int month, int day) {
    	Workout workout = new Workout(year, month, day);
    	WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutChestActivity.this);
    	handler.addWorkout(workout);
    	workout = handler.getWorkout(year, month, day);
    	handler.close();
    	if (workout != null) {
    		Toast.makeText(this, workout.getID(), Toast.LENGTH_LONG).show();
    	}
    	else {
    		Toast.makeText(this, "Not found", Toast.LENGTH_LONG).show();
    	}
    	return workout;
    }
}

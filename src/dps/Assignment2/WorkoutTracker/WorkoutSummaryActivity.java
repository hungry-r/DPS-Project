package dps.Assignment2.WorkoutTracker;

import java.util.ArrayList;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WorkoutSummaryActivity extends Activity {
	private static final String CURRENT_CATEGORY = "summary";
	private int mYear;
	private int mMonth;
	private int mDay;
	private TextView workoutDateText;
	private EditText weightField;

	private Button changeDateBtn;
	private Button addBtn;

	private LinearLayout exerciseDisplay;
	static final int DATE_DIALOG_ID = 0;

	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_summary);
        
        weightField = (EditText)findViewById(R.id.edit_weight);

        exerciseDisplay = (LinearLayout)findViewById(R.id.exercise_layout);
        
        // Set Workout Date Text to today as default
        workoutDateText = (TextView)findViewById(R.id.workoutDateText);
        Time now = new Time(Time.getCurrentTimezone());
        now.setToNow();
        mYear = now.year;
        mMonth = now.month + 1;
        mDay = now.monthDay;
        
        // If there is no existing selected date, then set it to the current date
        if (WorkoutStartTabWidget.selectedYear == -1) {
	        WorkoutStartTabWidget.selectedYear = mYear;
	        WorkoutStartTabWidget.selectedMonth = mMonth;
	        WorkoutStartTabWidget.selectedDay = mDay;
        }
        
        workoutDateText.setText(WorkoutStartTabWidget.selectedMonth + "/" + WorkoutStartTabWidget.selectedDay + "/" + WorkoutStartTabWidget.selectedYear);
        
        // If we can't find an existing workout database entry for this day, create a new one in database, and set the static workout object to it
        if (getWorkoutOn(WorkoutStartTabWidget.selectedYear,WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay) == null) {
        	WorkoutStartTabWidget.currentWorkout = createWorkoutOn(mYear, mMonth, mDay);
        	weightField.setText("");
        }
        else {
        	WorkoutStartTabWidget.currentWorkout = getWorkoutOn(WorkoutStartTabWidget.selectedYear, WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay);
        	WorkoutStartTabWidget.currentWorkout.setExerciseRecords(getExercisesForWorkout(WorkoutStartTabWidget.currentWorkout.getID()));
        	updateExerciseDisplay();
        }
        
        // Add listener to change date button
        changeDateBtn = (Button)findViewById(R.id.changeDateBtn);
        changeDateBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
        });
        
        // Add listener to update weight button
        addBtn = (Button)findViewById(R.id.weight_addBtn);
        addBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (weightField.getText().toString().equals("") ) {
					Toast.makeText(WorkoutSummaryActivity.this, "Fill out all required fields first.", Toast.LENGTH_LONG).show();
				}
				else {
					WorkoutStartTabWidget.currentWorkout.setBodyWeight(Integer.parseInt(weightField.getText().toString()));
					Toast.makeText(WorkoutSummaryActivity.this, Integer.toString(WorkoutStartTabWidget.currentWorkout.getBodyWeight()) , Toast.LENGTH_LONG).show();
					WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutSummaryActivity.this);
					handler.updateWorkout(WorkoutStartTabWidget.currentWorkout);
					handler.close();
				}
			}
        });
              
        // Update List of Exercises performed at the bottom, if applicable 
        updateExerciseDisplay();

    }
    
    public void onResume() {
    	super.onResume();
    	if (getWorkoutOn(WorkoutStartTabWidget.selectedYear,WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay) == null) {
        	WorkoutStartTabWidget.currentWorkout = createWorkoutOn(mYear, mMonth, mDay);
        	weightField.setText("");
        }
        else {
        	WorkoutStartTabWidget.currentWorkout = getWorkoutOn(WorkoutStartTabWidget.selectedYear, WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay);
        	WorkoutStartTabWidget.currentWorkout.setExerciseRecords(getExercisesForWorkout(WorkoutStartTabWidget.currentWorkout.getID()));
        	weightField.setText(Integer.toString(WorkoutStartTabWidget.currentWorkout.getBodyWeight()));
        }
    	workoutDateText.setText(WorkoutStartTabWidget.selectedMonth + "/" + WorkoutStartTabWidget.selectedDay + "/" + WorkoutStartTabWidget.selectedYear);
    	updateExerciseDisplay();
    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
	    new DatePickerDialog.OnDateSetListener() {
	
	        public void onDateSet(DatePicker view, int year, 
	                              int monthOfYear, int dayOfMonth) {
	        	WorkoutStartTabWidget.selectedYear = year;
	            WorkoutStartTabWidget.selectedMonth = monthOfYear + 1;
	            WorkoutStartTabWidget.selectedDay = dayOfMonth;
	            workoutDateText.setText(WorkoutStartTabWidget.selectedMonth + "/" + WorkoutStartTabWidget.selectedDay + "/" + WorkoutStartTabWidget.selectedYear);
	            if (getWorkoutOn(WorkoutStartTabWidget.selectedYear,WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay) == null) {
	            	WorkoutStartTabWidget.currentWorkout = createWorkoutOn(WorkoutStartTabWidget.selectedYear, WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay);
	            }
	            else {
	            	WorkoutStartTabWidget.currentWorkout = getWorkoutOn(WorkoutStartTabWidget.selectedYear, WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay);
	            	WorkoutStartTabWidget.currentWorkout.setExerciseRecords(getExercisesForWorkout(WorkoutStartTabWidget.currentWorkout.getID()));
	            }
	            updateExerciseDisplay();
	        }
	    };
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        WorkoutStartTabWidget.selectedYear, WorkoutStartTabWidget.selectedMonth - 1, WorkoutStartTabWidget.selectedDay);
        }
        return null;
    }
	
    private Workout getWorkoutOn(int year, int month, int day) {
    	Workout workout;
    	WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutSummaryActivity.this);
    	workout = handler.getWorkout(year, month, day);
    	handler.close();
    	return workout;
    }
    
    private Workout createWorkoutOn(int year, int month, int day) {
    	Workout workout = new Workout(year, month, day);
    	WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutSummaryActivity.this);
    	handler.addWorkout(workout);
    	workout = handler.getWorkout(year, month, day);
    	handler.close();
    	return workout;
    }
    
    private ArrayList<Exercise> getExercisesForWorkout(int id) {
    	ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
    	ArrayList<Exercise> tempList = new ArrayList<Exercise>();
    	WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutSummaryActivity.this);
    	tempList = handler.getAllExercisesFromWorkout(id);
    	for (int i = 0; i < tempList.size(); i++) {
    		exerciseList.add(tempList.get(i));
    	}
    	handler.close();
    	return exerciseList;
    }
    
    private void updateExerciseDisplay() {
    	ArrayList<Exercise> exercises = WorkoutStartTabWidget.currentWorkout.getAllExerciseRecords();
    	exerciseDisplay.removeAllViews();
    	for (int i = 0; i < exercises.size(); i++) {
    	  	TextView newEntry = new TextView(this);
    		String entryText = exercises.get(i).getName() + ":  " + exercises.get(i).getWeight() + " x " + exercises.get(i).getReps();
    		newEntry.setText((CharSequence)entryText);
    		exerciseDisplay.addView(newEntry);
    	}
    	
    }
}

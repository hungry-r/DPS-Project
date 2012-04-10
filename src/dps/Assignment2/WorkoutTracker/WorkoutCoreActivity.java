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

public class WorkoutCoreActivity extends Activity {
	private static final String CURRENT_CATEGORY = "core";
	private int mYear;
	private int mMonth;
	private int mDay;
	private TextView workoutDateText;
	private EditText weightField;
	private EditText repsField;
	private Button changeDateBtn;
	private Button addBtn;
	private Spinner exerciseSpinner;
	private LinearLayout exerciseDisplay;
	static final int DATE_DIALOG_ID = 0;

	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_core);
        
        weightField = (EditText)findViewById(R.id.core_edit_weight);
        repsField = (EditText)findViewById(R.id.core_edit_reps);
        exerciseDisplay = (LinearLayout)findViewById(R.id.core_exercise_layout);
        
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
        
        // Add listener to add exercise button
        addBtn = (Button)findViewById(R.id.core_addBtn);
        addBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (repsField.getText().toString().equals("") || weightField.getText().toString().equals("") ) {
					Toast.makeText(WorkoutCoreActivity.this, "Fill out all required fields first.", Toast.LENGTH_LONG).show();
				}
				else {
					Exercise newExercise = new Exercise(exerciseSpinner.getSelectedItem().toString(), CURRENT_CATEGORY, 
							Integer.parseInt(weightField.getText().toString()), Integer.parseInt(repsField.getText().toString()), 
							WorkoutStartTabWidget.currentWorkout.getID());
					WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutCoreActivity.this);
					handler.addWorkoutExercise(newExercise);
					handler.close();
					WorkoutStartTabWidget.currentWorkout.addExercise(newExercise);
					Toast.makeText(WorkoutCoreActivity.this, "Added Exercise", Toast.LENGTH_LONG).show();
					updateExerciseDisplay();
				}
			}
        });
      
        // Populate spinner
        exerciseSpinner = (Spinner) findViewById(R.id.spinner_core);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.exercises_array_core, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);
        
        // Update List of Core Exercises performed at the bottom, if applicable 
        updateExerciseDisplay();

    }
    
    public void onResume() {
    	super.onResume();
    	if (getWorkoutOn(WorkoutStartTabWidget.selectedYear,WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay) == null) {
        	WorkoutStartTabWidget.currentWorkout = createWorkoutOn(mYear, mMonth, mDay);
        }
        else {
        	WorkoutStartTabWidget.currentWorkout = getWorkoutOn(WorkoutStartTabWidget.selectedYear, WorkoutStartTabWidget.selectedMonth, WorkoutStartTabWidget.selectedDay);
        	WorkoutStartTabWidget.currentWorkout.setExerciseRecords(getExercisesForWorkout(WorkoutStartTabWidget.currentWorkout.getID()));
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
                        mYear, mMonth, mDay);
        }
        return null;
    }
	
    private Workout getWorkoutOn(int year, int month, int day) {
    	Workout workout;
    	WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutCoreActivity.this);
    	workout = handler.getWorkout(year, month, day);
    	handler.close();
    	return workout;
    }
    
    private Workout createWorkoutOn(int year, int month, int day) {
    	Workout workout = new Workout(year, month, day);
    	WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutCoreActivity.this);
    	handler.addWorkout(workout);
    	workout = handler.getWorkout(year, month, day);
    	handler.close();
    	return workout;
    }
    
    private ArrayList<Exercise> getExercisesForWorkout(int id) {
    	ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
    	ArrayList<Exercise> tempList = new ArrayList<Exercise>();
    	WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutCoreActivity.this);
    	tempList = handler.getAllExercisesFromWorkout(id);
    	for (int i = 0; i < tempList.size(); i++) {
    		exerciseList.add(tempList.get(i));
    	}
    	handler.close();
    	return exerciseList;
    }
    
    private void updateExerciseDisplay() {
    	ArrayList<Exercise> coreExercises = WorkoutStartTabWidget.currentWorkout.getExercisesByCategory(CURRENT_CATEGORY);
    	exerciseDisplay.removeAllViews();
    	for (int i = 0; i < coreExercises.size(); i++) {
    	  	TextView newEntry = new TextView(this);
    		String entryText = coreExercises.get(i).getName() + ":  " + coreExercises.get(i).getWeight() + " x " + coreExercises.get(i).getReps();
    		newEntry.setText((CharSequence)entryText);
    		exerciseDisplay.addView(newEntry);
    	}
    	
    }
}

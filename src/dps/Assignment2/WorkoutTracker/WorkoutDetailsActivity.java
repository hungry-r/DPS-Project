package dps.Assignment2.WorkoutTracker;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkoutDetailsActivity extends Activity {
	private int year;
	private int month;
	private int day;
	private Workout workout;
	private ArrayList<Exercise> exercises;
	private TextView pageLabel;
	private LinearLayout exerciseDisplay;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.workout_details);
	     
	     month = getIntent().getExtras().getInt("month");
	     day = getIntent().getExtras().getInt("day");
	     year = getIntent().getExtras().getInt("year");
	     
	     WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutDetailsActivity.this);
	     workout = handler.getWorkout(year, month, day);
	     exercises = handler.getAllExercisesFromWorkout(workout.getID());
	     pageLabel = (TextView)findViewById(R.id.workoutDetailTitle);
	     pageLabel.setText("Details for " + workout.toString());
	     exerciseDisplay = (LinearLayout)findViewById(R.id.detail_exercise_layout);
	     
	     TextView bodyWeightEntry = new TextView(this);
	     bodyWeightEntry.setText("Body weight: " + Integer.toString(workout.getBodyWeight()) + " lbs");
	     exerciseDisplay.addView(bodyWeightEntry);
	     
	     for (int i = 0; i < exercises.size(); i++) {
    	  	TextView newEntry = new TextView(this);
    		String entryText = exercises.get(i).getName() + ":  " + exercises.get(i).getWeight() + " x " + exercises.get(i).getReps();
    		newEntry.setText((CharSequence)entryText);
    		exerciseDisplay.addView(newEntry);
    	}
	     
	}

}

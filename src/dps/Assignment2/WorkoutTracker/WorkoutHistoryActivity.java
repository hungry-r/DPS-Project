package dps.Assignment2.WorkoutTracker;

import java.util.ArrayList;

import android.app.ListActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
public class WorkoutHistoryActivity extends ListActivity {
	private ArrayList<Workout> workoutList;
	private String[] workoutStringArray;
	
	static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
			"Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
			"Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WorkoutDbHelper handler = new WorkoutDbHelper(WorkoutHistoryActivity.this);
		workoutList = handler.getAllWorkouts();
		
		workoutStringArray = new String[workoutList.size()];
		for (int i = 0; i < workoutList.size(); i++) {
			workoutStringArray[i] = workoutList.get(i).toString();
		}
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.workout_history, workoutStringArray));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			   Workout selectedWorkout = workoutList.get(position);
			   Intent intent = new Intent(WorkoutHistoryActivity.this, WorkoutDetailsActivity.class);
			   Bundle bundle = new Bundle();
			   bundle.putInt("month", selectedWorkout.getMonth());
			   bundle.putInt("day", selectedWorkout.getDay());
			   bundle.putInt("year", selectedWorkout.getYear());
			   intent.putExtras(bundle);
			   startActivity(intent);
			}
		});
 
	}
 
}
package dps.Assignment2.WorkoutTracker;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class WorkoutStartTabWidget extends TabActivity {
	
	public static int selectedYear = -1;
	public static int selectedMonth = -1;
	public static int selectedDay = -1;
	public static Workout currentWorkout;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabs);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, WorkoutChestActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("chest").setIndicator("Chest")
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, WorkoutLegsActivity.class);
	    spec = tabHost.newTabSpec("legs").setIndicator("Legs")
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, WorkoutBackActivity.class);
	    spec = tabHost.newTabSpec("back").setIndicator("Back")
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, WorkoutCoreActivity.class);
	    spec = tabHost.newTabSpec("core").setIndicator("Core")
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, WorkoutArmsActivity.class);
	    spec = tabHost.newTabSpec("arms").setIndicator("Arms")
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, WorkoutCardioActivity.class);
	    spec = tabHost.newTabSpec("cardio").setIndicator("Cardio")
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.getTabWidget().getChildAt(0).getLayoutParams().width = 140;
	    tabHost.getTabWidget().getChildAt(1).getLayoutParams().width = 140;
	    tabHost.getTabWidget().getChildAt(2).getLayoutParams().width = 140;
	    tabHost.getTabWidget().getChildAt(3).getLayoutParams().width = 140;
	    tabHost.getTabWidget().getChildAt(4).getLayoutParams().width = 140;
	    tabHost.getTabWidget().getChildAt(5).getLayoutParams().width = 140;
	    
	    // Set default tab
	    tabHost.setCurrentTab(0);
	   
	}
}

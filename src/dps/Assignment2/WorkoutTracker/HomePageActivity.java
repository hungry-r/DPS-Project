package dps.Assignment2.WorkoutTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomePageActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        
        Button workoutStartBtn = (Button)findViewById(R.id.startBtn);
        Button workoutHistoryBtn = (Button)findViewById(R.id.historyBtn);
        Button workoutAlarmBtn = (Button)findViewById(R.id.alarmBtn);
        Button workoutChartBtn = (Button)findViewById(R.id.chartBtn);
        
        workoutStartBtn.setOnClickListener(startBtnListener);
        workoutHistoryBtn.setOnClickListener(historyBtnListener);
        workoutAlarmBtn.setOnClickListener(alarmBtnListener);
        workoutChartBtn.setOnClickListener(chartBtnListener);
    }
    
    private OnClickListener startBtnListener = new OnClickListener() {
		public void onClick(View v) {
			startWorkoutActivity();
		}
    };
    
    private OnClickListener historyBtnListener = new OnClickListener() {
		public void onClick(View v) {
			startHistoryActivity();			
		}
    };
    
    private OnClickListener alarmBtnListener = new OnClickListener() {
		public void onClick(View v) {
			startAlarmActivity();			
		}
    };
    
    private OnClickListener chartBtnListener = new OnClickListener() {
		public void onClick(View v) {
			startChartActivity();			
		}
    };
    
    void startWorkoutActivity() {
    	Intent i = new Intent(this, WorkoutStartTabWidget.class);
    	startActivity(i);
    }
    
    void startAlarmActivity() {
    	Intent i = new Intent(this, SetAlarmActivity.class);
    	startActivity(i);
    }
    
    void startChartActivity() {
    	Intent i = new Intent(this, WorkoutChartActivity.class);
    	startActivity(i);
    }
    
    void startHistoryActivity() {
    	Intent i = new Intent(this, WorkoutHistoryActivity.class);
    	startActivity(i);
    }
};
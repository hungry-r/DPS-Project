package dps.Assignment2.WorkoutTracker;


import android.app.AlertDialog;
import android.app.Service;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.IBinder;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



public class MyAlarmService extends Service {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
	}
	
	@Override
		public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
	}
	
	@Override
	
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	
		/*Intent i = new Intent(getBaseContext(), DPS914ProjectActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplication().startActivity(i);*/
		Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
		return super.onUnbind(intent);
	}
}
package dps.Assignment2.WorkoutTracker;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorkoutDbHelper extends SQLiteOpenHelper {

	private static final String TAG = WorkoutDbHelper.class.getSimpleName();
	// database dame
	private static final String DATABASE_NAME = "workoutdata.db";
	// Workout table name
	private static final String TABLE_WORKOUT = "workout_table";
	// Exercsies table name
	private static final String TABLE_EXERCISE = "exercise_table";
	// database version
	private static final int DATABASE_VERSION = 1; // increase the number after
													// you change table
													// structure
	
	private static final String CREATE_WORKOUT_TABLE = 
			"create table workout_table (_id integer primary key autoincrement, "
			+ "_year integer not null,"
			+ "_month integer not null,"
			+ "_day integer not null,"
			+ "_hour integer not null,"
			+ "_minute integer not null,"
			+ "_weight_body integer not null" + ");";
	
	private static final String CREATE_EXERCISE_TABLE = 
		"create table exercise_table (_id integer primary key autoincrement, "
		+ "_name text not null,"
		+ "_category text not null,"
		+ "_weight integer not null,"
		+ "_reps integer not null,"
		+ "_parent_workout integer not null," 
		+ "FOREIGN KEY (_parent_workout) REFERENCES workout_table(_id)" + ");";

	// workout table column names
	public static final String KEY_WORKOUT_ID = "_id";
	public static final String KEY_YEAR = "_year";
	public static final String KEY_MONTH = "_month";
	public static final String KEY_DAY = "_day";
	public static final String KEY_HOUR = "_hour";
	public static final String KEY_MINUTE = "_minute";
	public static final String KEY_WEIGHT_BODY = "_weight_body";
	
	// workout table column names
	public static final String KEY_EXERCISE_ID = "_id";
	public static final String KEY_NAME = "_name";
	public static final String KEY_CATEGORY = "_category";
	public static final String KEY_WEIGHT = "_weight";
	public static final String KEY_REPS = "_reps";
	public static final String KEY_PARENT_WORKOUT = "_parent_workout";
	
	
	public WorkoutDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create table(s)
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate sql " + CREATE_WORKOUT_TABLE);
		db.execSQL(CREATE_WORKOUT_TABLE);
		
		Log.d(TAG, "onCreate sql " + CREATE_EXERCISE_TABLE);
		db.execSQL(CREATE_EXERCISE_TABLE);
	}

	// upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade sql " + TABLE_WORKOUT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
		
		Log.d(TAG, "onUpgrade sql " + TABLE_EXERCISE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
		
		onCreate(db);
	}

	/**
	 * implement CRUD(Create, Read, Update, Delete) operations
	 */

	// create new workout
	void addWorkout(Workout workout) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_YEAR, workout.getYear());
		values.put(KEY_MONTH, workout.getMonth());
		values.put(KEY_DAY, workout.getDay());
		values.put(KEY_HOUR, workout.getHour());
		values.put(KEY_MINUTE, workout.getMinute());
		values.put(KEY_WEIGHT_BODY, workout.getBodyWeight());
		
		Log.d(TAG, "insert: " + workout.getYear() + "/" + workout.getMonth() + "/" + workout.getDay());

		db.insert(TABLE_WORKOUT, null, values);
		db.close();
	}

	// get single workout
	Workout getWorkout(int year, int month, int day) {
		Workout workout;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_WORKOUT, new String[] { KEY_WORKOUT_ID,
				KEY_YEAR, KEY_MONTH, KEY_DAY, KEY_HOUR, KEY_MINUTE, KEY_WEIGHT_BODY}, KEY_YEAR
				+ "=? AND " + KEY_MONTH + "=? AND " + KEY_DAY + "=?", new String[] { String.valueOf(year), String.valueOf(month), String.valueOf(day) }, 
				null, null, null, null/* , null, null */);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			workout = new Workout(Integer.parseInt(cursor.getString(1)),
				Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)),
				Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(6)));
		}
		else {
			workout = null;
		}
		
		db.close();
		return workout;
	}
	
	// get all contacts
	public ArrayList<Workout> getAllWorkouts() {
		ArrayList<Workout> workoutList = new ArrayList<Workout>();

		String selectQuery = "SELECT  * FROM " + TABLE_WORKOUT;

		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// loop to all rows to the list
		if (cursor.moveToFirst()) {
			do {
				Workout workout = new Workout(Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), 
						Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
						Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(6)));

				// Adding workout to list
				workoutList.add(workout);
			} while (cursor.moveToNext());
		}
		db.close();
		return workoutList;
	}

	// get all contacts
	public ArrayList<Exercise> getAllExercisesFromWorkout(int id) {
		ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE;

		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_EXERCISE, new String[] { KEY_EXERCISE_ID,
				KEY_NAME, KEY_CATEGORY, KEY_WEIGHT, KEY_REPS}, KEY_PARENT_WORKOUT
				+ "=?" , new String[] { String.valueOf(id) }, 
				null, null, null, null/* , null, null */);

		// loop to all rows to the list
		if (cursor.moveToFirst()) {
			do {
				Exercise exercise = new Exercise(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), 
						Integer.parseInt(cursor.getString(4)), id, Integer.parseInt(cursor.getString(0)));

				// Adding exercise to list
				exerciseList.add(exercise);
			} while (cursor.moveToNext());
		}
		db.close();
		return exerciseList;
	}

	// add an exercise belonging to a workout
	public void addWorkoutExercise(Exercise exercise) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, exercise.getName());
		values.put(KEY_CATEGORY, exercise.getCategory());
		values.put(KEY_WEIGHT, exercise.getWeight());
		values.put(KEY_REPS, exercise.getReps());
		values.put(KEY_PARENT_WORKOUT, exercise.getParentWorkoutID());
		
		Log.d(TAG, "insert: " + exercise.getName());

		db.insert(TABLE_EXERCISE, null, values);
		db.close();
	}
	
	// Update a Workout entry in the database
	public int updateWorkout(Workout workout) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_YEAR, workout.getYear());
		values.put(KEY_MONTH, workout.getMonth());
		values.put(KEY_DAY, workout.getDay());
		values.put(KEY_HOUR, workout.getHour());
		values.put(KEY_MINUTE, workout.getMinute());
		values.put(KEY_WEIGHT_BODY, workout.getBodyWeight());
		
		int returnValue = db.update(TABLE_WORKOUT, values, KEY_WORKOUT_ID + "= ?", new String[] { String.valueOf(workout.getID())});
		db.close();
		return returnValue;
	}

	/* 
	// delete a contact
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ROW_ID + " = ?",
				new String[] { String.valueOf(contact.getId()) });
		db.close();
	}

	// remove all contacts from database.
	public void removeAll() {
		// db.delete(String tableName, String whereClause, String[] whereArgs);
		// If whereClause is null, it will delete all rows.
		SQLiteDatabase db = this.getWritableDatabase(); 
		db.delete(TABLE_CONTACTS, null, null);
		db.close();
	}

	// get contacts count
	public int getContactsCount() {
		String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}*/

}

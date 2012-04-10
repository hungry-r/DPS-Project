package dps.Assignment2.WorkoutTracker;

public class Exercise {
	private String name;
	private String category;
	private int id;
	private int weight;
	private int reps;
	private int parent_workout_id;
	
	// This constructor is called when instantiating an Exercise object in application, and is impossible to know the database row ID.
	public Exercise(String name, String category, int weight, int reps, int parent_workout_id) {
		this.name = name;
		this.category = category;
		this.weight = weight;
		this.reps = reps;
		this.parent_workout_id = parent_workout_id;
	}
	
	// This constructor is called when instantiating an Exercise object by loading data from the SQLite database.
	public Exercise(String string, String string2, int weight, int reps, int parent_workout_id, int id) {
		this.name = string;
		this.category = string2;
		this.weight = weight;
		this.reps = reps;
		this.parent_workout_id = parent_workout_id;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getReps() {
		return reps;
	}
	
	public int getID() {
		return id;
	}
	
	public int getParentWorkoutID() {
		return parent_workout_id;
	}
}

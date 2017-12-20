package leggettm.workoutlog;

import android.support.annotation.NonNull;


/**
 * Workout
 * Constructs workout objects
 *
 * @author Mel Leggett
 * Date 1/29/2017.
 */

public class Workout implements Comparable<Workout> {

    //class level variables
    private int workID; //the id of the workout built from strings
    private int serial; //the workId to add to dateStamp to form workID
    private String dateStamp;
    private String date; //the day and month of the workout in String format
    private String name; //name of the workout
    private float weight; //the amount of weight lifted
    private int sets; //the amount of sets performed
    private int reps; //the amount of repetitions per set

    /**
     * Default Constructor for the Workout object
     */
    public Workout() {

    }

    /**
     * Overloaded Constructor to build a workout object with the
     * user's input.
     *
     * @param name   Name of the workout(ie. curls, squats etc)
     * @param weight The amount lifted
     * @param sets   The number of full sets done for this workout
     * @param reps   The amount of repetitions done in each set
     */
    public Workout(String name, float weight, int sets, int reps) {
        this.name = name;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
    }

    //--------------- Setters & Getters ---------------------------------------

    /**
     * Provides the id of the workout
     * @return workID number
     */
    public int getWorkID() {
        return workID;
    }

    /**
     * sets the workID of the workout(done through DataManager)
     * @param workID id number of the workout
     */
    public void setWorkID(int workID) {
        this.workID = workID;
    }


    /**
     * Provides access to the name of the exercise
     *
     * @return the name of the exercise
     */
    public String getName() {
        return name;
    }

    /**
     *  Sets the name of workout being done
     * @param name of the workout
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Provides access to the weight being lifted
     *
     * @return the weight lifted
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Allows user to set the weight lifted
     *
     * @param weight of the exercise
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Provides access to the number of sets done for the exercise
     *
     * @return the number of full sets completed
     */
    public int getSets() {
        return sets;
    }

    /**
     * Allows user to set the number of sets done
     *
     * @param sets done for the workout
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    /**
     * Provides access to the number of repetitions done per set
     *
     * @return the reps in each set
     */
    public int getReps() {
        return reps;
    }

    /**
     * Allows user to set the reps done
     *
     * @param reps done in the workout
     */
   public void setReps(int reps) {
        this.reps = reps;
    }

   public String getDateStr() {
        String month = this.getDateStamp().substring(2,4);
        String day = this.getDateStamp().substring(4,6);
        return Year.digitToMonth(month).concat(day);
    }

    /**
     * Gives access to the serial(the begining of the workID based on date
     * @return serial number builder for the object
     */
    public int getSerial() {
        return serial;
    }

    /**
     * Sets the serial number of the workout based on year, month, day
     * @param serial the serial number(date code) of the object
     */
    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    /**
     * String version of the date for display
     * @return date string for display
     */
    public String getDate() {
        return date;
    }

    /**
     * sets the date of the workout in string form ie. Feb
     * @param date the date of the workout
     */
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {

        return name + ",      " + weight +"lbs.";
    }

    public int compareTo(@NonNull Workout other) {
        return this.dateStamp.compareTo(other.dateStamp);
    }
}
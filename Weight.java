package leggettm.workoutlog;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Weight
 * Constructs Weight objects
 *
 * @author Mel Leggett
 * Date 2/07/2017.
 */
class Weight implements Comparable<Weight> {
    //instance variables
    private int weightID;
    private String place;
    private String sorter;
    private float weight;
    private float bmi;
    private int systolic;
    private int diastolic;
    private String month;
    private int day;
    //months
    private List<String> jan;
    private List<String> feb;
    private List<String> mar;
    private List<String> apr;
    private List<String> may;
    private List<String> jun;
    private List<String> jul;
    private List<String> aug;
    private List<String> sep;
    private List<String> oct;
    private List<String> nov;
    private List<String> dec;


    /**
     * Default constructor for Weight object
     */
    Weight() {
    }

    /**
     * Overloaded constructor to build the Weight object
     * @param weight the weight of the user
     * @param bmi the body mass index of the user
     * @param systolic the systolic (blood pressure) of user
     * @param diastolic the diastolic (blood pressure) of user
     */
    public Weight(String month, int day, float weight, float bmi, int systolic, int diastolic) {
        this.month = month;
        this.day = day;
        this.weight = weight;
        this.bmi = bmi;
        this.systolic = systolic;
        this.diastolic = diastolic;
    }


    //************** Getters & Setters *************************


    public int getWeightID() {
        return weightID;
    }

    public void setWeightID(int weightID) {
        this.weightID = weightID;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {this.day = day; }

    public String getPlace() {
        return place;
    }

    public void setPlace(String monthCode, int dayCode) {
        String tempDay = String.valueOf(dayCode);
        if(tempDay.length() < 2)
            tempDay = "0" + tempDay;
        this.place = monthCode.concat(tempDay);
    }

    public String getSorter() {
        return sorter;
    }

    public void setSorter(String place, int id) {
        sorter = place.concat(String.valueOf(id));
    }

    @Override
    public String toString() {
        String out;
        if(day == 1)
            out = month + " 0" + day + "st";
        else if(day == 2)
            out = month + " 0" + day + "nd";
        else if(day == 3)
            out = month + " 0" + day + "rd";
        else if(day < 10)
            out = month + " 0" + day + "th";
        else
            out = month +" " + day + "th";
        return out + "--     " + weight + "lbs";
    }
    public int compareTo(@NonNull Weight other) {
        return this.getSorter().compareToIgnoreCase(other.getSorter());
    }
}

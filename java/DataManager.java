package leggettm.workoutlog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DataManager
 * Stores the list of workouts, sets them IDs, and general database operations
 * @author Mel Leggett
 * Date 01/29/2017
 * */
public class DataManager {

    //class level variables
    private static DataManager dm;
    private List<Workout> workList;
    private int nextWorkID = 1000; //ensures no redundant entries
    private List<Weight> weightList;
    private int nextWeightID = 1000; //ensures no redundant entries
    private List<Year> yearList;
    /**
     * default constructor creates the new lists
     */
    private DataManager() {
        workList = new ArrayList<>();
        weightList = new ArrayList<>();
        yearList = new ArrayList<>();
    }

    /**
     * if empty creates a new DataManager, otherwise returns the existing
     * @return the DataManager
     */
    public static DataManager getDataManager() {
        if(dm == null) {
            dm = new DataManager();
        }
        return dm;
    }

    //*************** Workout Section ****************************

    /**
     * List of all the workouts
     * @return workList of all workouts
     */
    List<Workout> getWorkList() {
        Collections.sort(workList);
        return workList;
    }

    public void setWorkList(List<Workout> workList) {
        this.workList = workList;
        if(workList == null) {
            workList = new ArrayList<>();
            nextWorkID = 1000;
        }
        if(workList.size() > 0) { //find the next workID number to use
            for(Workout w: workList) {
                if(w.getSerial() > nextWorkID) {
                    nextWorkID = w.getSerial();
                }
                nextWorkID++; //incrament to set new number to asign for new workout
            }
        }
    }
    /**
     * Gets the individual workout in question by id
     * @param id the number for the workout
     * @return Workout that was searched for
     */
    public Workout getWorkout(int id) {
        //search for id
        int index = -1;
        for(int i = 0; i < workList.size(); i++) {
            Workout wrkOut = workList.get(i);
            if(wrkOut.getWorkID() == id) {
                index = i;
                break;
            }
        }
        if(index >= 0) { //found, return index
            return workList.get(index);
        } else {
            return null;
        }
    }

    /**
     * Adds a new workout and assigns it an id, incraments id counter
     * @param newWork newWorkout to be added
     */
    public void addWorkout(Workout newWork, String fireDir) {
        if(newWork == null) {
            return;
        }
        newWork.setSerial(nextWorkID);
        newWork.setWorkID(Integer.parseInt(newWork.getDateStamp().
                concat(String.valueOf(newWork.getSerial()))));
        newWork.setDate(newWork.getDateStr());
        workList.add(newWork);
        nextWorkID++;
        Collections.sort(workList);
        DatabaseReference ref = FirebaseDatabase.
                getInstance().getReference(fireDir);
        ref.push();
        ref.setValue(workList);
    }

    /**
     * removes the workout based on the id
     * @param id to find the workout in question
     */
    public void delWorkout(int id, String fireDir) {
        int index = -1;
        for (int i = 0; i < workList.size() && index < 0; i++) {
            Workout w = workList.get(i);
            if (w.getWorkID() == id) {
                index = i;
            }
        }
        if (index >= 0) { //found, remove the workout
            workList.remove(index);
            DatabaseReference ref = FirebaseDatabase.
                    getInstance().getReference(fireDir);
            ref.push();
            ref.setValue(workList);
        }
    }

    public void editWorkout(String fireDir) {
        DatabaseReference ref = FirebaseDatabase.
                getInstance().getReference(fireDir);
        ref.push();
        ref.setValue(workList);
    }

    //********* Weight section ***********************
    /**
     * List of all the weigh ins
     * @return weightList of all weigh ins
     */
    public List<Weight> getWeightList() {
        Collections.sort(weightList);
        return weightList;
    }

    public void setWeightList(List<Weight> weightList) {
        this.weightList = weightList;
        if(weightList == null) {
            weightList = new ArrayList<>();
            nextWeightID = 1000;
        }
        if(weightList.size() > 0) { //find the next weightID number to use
            for(Weight w: weightList) {
             if(w.getWeightID() > nextWeightID) {
                 nextWeightID = w.getWeightID();
             }
                nextWeightID++; //incrament to set new number to asign for new weight
            }
        }
    }

    /**
     * Gets the individual weight in question by id
     * @param id the number for the weigh in
     * @return Weight that was searched for
     */
    public Weight getWeight(int id) {
        //search for id
        int index = -1;
        for(int i = 0; i < weightList.size(); i++) {
            Weight weigh = weightList.get(i);
            if(weigh.getWeightID() == id) {
                index = i;
                break;
            }
        }
        if(index >= 0) { //found, return index
            return weightList.get(index);
        } else {
            return null;
        }
    }

    /**
     * Adds a new weigh in and assigns it an id, increments id counter
     * may not be necessary later on
     * @param newWeight new Weigh in to be added
     */
    public void addWeight(Weight newWeight, String fireDir) {
        if(newWeight == null) {
            return;
        }
        newWeight.setWeightID(nextWeightID);
        newWeight.setSorter(newWeight.getPlace(), newWeight.getWeightID());
        weightList.add(newWeight);
        nextWeightID++;
        Collections.sort(weightList);
        DatabaseReference ref = FirebaseDatabase.
                getInstance().getReference(fireDir);
        ref.push();
        ref.setValue(weightList);
    }

    /**
     * removes the weigh in based on the id
     * @param id to find the weight in question
     */
    public void delWeight(int id, String fireDir) {
        int index = -1;
        for (int i = 0; i < weightList.size() && index < 0; i++) {
            Weight w = weightList.get(i);
            if (w.getWeightID() == id) {
                index = i;
            }
        }
        if (index >= 0) { //found, remove the weigh in
            weightList.remove(index);
            Collections.sort(weightList);
            DatabaseReference ref = FirebaseDatabase.
                    getInstance().getReference(fireDir);
            ref.push();
            ref.setValue(weightList);
        }
    }

    public void editWeight(String fireDir) {
        Collections.sort(weightList);
        DatabaseReference ref = FirebaseDatabase.
                getInstance().getReference(fireDir);
        ref.push();
        ref.setValue(weightList);
    }

    //*************** Months ***************************************88
    public List<Year> getYearList() {
        return yearList;
    }
    public Year getYear(int requestedYear) {
        int index = -1;
        for(int i = 0; i < yearList.size(); i++) {
            if(yearList.get(i).getName() == requestedYear) {
                index = i;
            }
        }
        if(index >= 0) {
            return yearList.get(index);
        }
        Year newYear = new Year(requestedYear);
        yearList.add(newYear);
        return newYear;
    }
    public void setYearList(List<Year> monthList) {
        this.yearList = monthList;
    }

    public void editMonth(String fireDir) {
        DatabaseReference ref = FirebaseDatabase.
                getInstance().getReference(fireDir);
        ref.push();
        ref.setValue(yearList);
    }
}

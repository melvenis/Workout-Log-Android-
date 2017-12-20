package leggettm.workoutlog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * WorkoutDetails Activity
 * Description Displays the details of the workout that was selected
 * on the list page.  Allows user to edit or delete the workout and refreshes
 * the changes to the edit
 */
public class WorkoutDetailsActivity extends CoreActivity {

    //class instance variables
    private int workId;
    private DataManager dm;
    public static final String EDIT_WORK = "edit work"; //for Intent pass
    private Workout myWorkout;
    private DatabaseReference workListRef;
    private ValueEventListener workValueEventListener;
    private String fireDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get selected workout
        dm = DataManager.getDataManager();
        Intent intent = getIntent();
        workId = intent.getIntExtra(WorkoutListActivity.WORKOUT_ID, -1);

        if (workId < 0) { //no workout was selected, close
            finish();
        }
        populateFields(); //display workout details
    }

    public void onResume() {
        super.onResume();
        populateFields();
    }

    /**
     * After locating the workout Id, all details are loaded for the user
     */
    private void populateFields() {
        Workout myWorkout = dm.getWorkout(workId);
        //populate labels with the details of the workout
        //name
        TextView name = (TextView) findViewById(R.id.lblName);
        name.setText(myWorkout.getName());
        //sets
        TextView sets = (TextView) findViewById(R.id.lblSets);
        sets.setText(String.valueOf(myWorkout.getSets()));
        //reps
        TextView reps = (TextView) findViewById(R.id.lblReps);
        reps.setText(String.valueOf(myWorkout.getReps()));
        //weight
        TextView weight = (TextView) findViewById(R.id.lblWeight);
        weight.setText(String.valueOf(myWorkout.getWeight()));

    }

    /**
     * Sends an intent result to the AddWorkoutActivity to edit
     * the workout shown
     * @param view the edit button
     */
    public void btnEditOnClick(View view) {
        Intent intent = new Intent(this, AddWorkoutActivity.class);
        intent.putExtra(EDIT_WORK, workId);
        startActivity(intent);
    }

    /**
     * Deletes the workout shown and returns to the list page
     * source for alertDialog: stackOverflow
     * http://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
     * @param view the delete button
     */
    public void btnDelOnClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete workout")
                .setMessage("Delete Workout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dm.delWorkout(workId, fireDir);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void setUpValueEventListeners() {
        fireDir = "worklist/" + userId;
        workListRef = FirebaseDatabase.getInstance().getReference(fireDir);
        if(workValueEventListener == null) {
            workValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<Workout>> t = new GenericTypeIndicator<List<Workout>>() {
                    };
                    List<Workout> workList = dataSnapshot.getValue(t);
                    if(workList == null) {
                        workList = new ArrayList<>();
                    }
                    dm.setWorkList(workList);
                    myWorkout = dm.getWorkout(workId);
                    populateFields();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //value was not read
                    Log.w("MYLOG", "Failed to read work list.", databaseError.toException());
                }
            };
        }
        workListRef.addValueEventListener(workValueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (workValueEventListener != null) {
            workListRef.removeEventListener(workValueEventListener);
        }
        workValueEventListener = null;
    }
}

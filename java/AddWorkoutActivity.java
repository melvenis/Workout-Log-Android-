package leggettm.workoutlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * AddWorkoutActivity
 * Description Takes intent from a variety of other activities and determines whether
 * they are add or edit based on the paramaters sent to it in the intent.
 * Also will determine which form sent it and if it needs an Intent Result
 * @author Mel Leggett
 * date 1/29/07
 */
public class AddWorkoutActivity extends CoreActivity {

    //class variables
    private DataManager dm;
    private String dateStamp; //serial builder
    private int workId; //workout number of the item passed from Details to edit
    private Workout myWork;
    private DatabaseReference workListRef;
    private ValueEventListener workValueEventListener;
    private String fireDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView dateLabel = (TextView)findViewById(R.id.lblDateDisplay);
        //verify if add or edit dialog based on whether an ID was sent in intent
        Intent intent = getIntent();
        dm = DataManager.getDataManager();
        workId = intent.getIntExtra(WorkoutDetailsActivity.EDIT_WORK, -1);
        if(workId < 0) { //not an edit, find info for add workout
            dateStamp = intent.getStringExtra(WorkoutListActivity.WORKOUT_DATE_STAMP);
            String tempMonth = Year.digitToMonth(dateStamp.substring(2,4));
            tempMonth = tempMonth.concat(" ").concat(dateStamp.substring(4,6));
            dateLabel.setText(tempMonth);
        } else {
            //Edit type workout
            if(getSupportActionBar()!= null) {
                getSupportActionBar().setTitle("Edit Workout");
                populateFields();
            }
        }
    }

    private void populateFields() {
        myWork = dm.getWorkout(workId);
        //find and display date
        String tempMonth = Year.digitToMonth(myWork.getDateStamp().substring(2,4));
        tempMonth = tempMonth.concat(" ").concat(myWork.getDateStamp().substring(4,6));
        TextView dateLabel = (TextView)findViewById(R.id.lblDateDisplay);
        dateLabel.setText(tempMonth);
        //Display current fields to be edited
        //name
        EditText name = (EditText) findViewById(R.id.txtName);
        name.setText(myWork.getName());
        //Sets
        EditText sets = (EditText) findViewById(R.id.txtSets);
        sets.setText(String.valueOf(myWork.getSets()));
        //Reps
        EditText reps = (EditText) findViewById(R.id.txtReps);
        reps.setText(String.valueOf(myWork.getReps()));
        //Weight
        EditText weight = (EditText) findViewById(R.id.txtWeightIn);
        weight.setText(String.valueOf(myWork.getWeight()));
    }
    /**
     * nullCheck to ensure all the fields have data before attempting
     * to build an object, if any are blank, do not add object and alert
     * the user to fill in the fields
     * @return boolean of whether the nullcheck passed or failed
     */
    public boolean nullCheck() {
        EditText name = (EditText) findViewById(R.id.txtName);
        if(name.getText().toString().isEmpty()) {
            return false;
            //change back color weight.setBackground();
        }
        EditText sets = (EditText) findViewById(R.id.txtSets);
        if(sets.getText().toString().isEmpty()) {
            return false;
        }
        EditText reps = (EditText) findViewById(R.id.txtReps);
        if(reps.getText().toString().isEmpty()) {
            return false;
        }
        EditText weight = (EditText)findViewById(R.id.txtWeightIn);
        return !weight.getText().toString().isEmpty();
    }

    /**
     * validateFields to ensure proper input is given for each variable type
     * if any field has a NumberFormatException, the user is notified as to which
     * field caused the error and no object is added until the test passes
     * @return integer indicating if test failed (if return < 0) and which field
     * specifically failed based on the value of the returned int
     */
    public int validateFields() {
        EditText sets = (EditText) findViewById(R.id.txtSets);
        try {
            int test = Integer.valueOf(sets.getText().toString());
        } catch(NumberFormatException ex) { //incorrect format
            return -1;
        }
        try {
            EditText reps = (EditText) findViewById(R.id.txtReps);
            int test = Integer.valueOf(reps.getText().toString());
        } catch(NumberFormatException ex) {
            return -2;
        }
        try {
            EditText weight = (EditText) findViewById(R.id.txtWeightIn);
            float test = Float.valueOf(weight.getText().toString());
        } catch(NumberFormatException ex) {
            return -3;
        }
        return 0; //all validations passed, return pass: 0
    }

    /**
     * Saves the data, whether new or edited to the datamanager and closes
     * the form
     * @param view the Save button
     */
    public void btnSaveOnClick(View view) {
        if(nullCheck()) { //empty test passed, proceed
            int valid = validateFields();
            if(valid == 0) { //validate entry passed, proceed
                Workout w;
                boolean add = false;
                //check if workID was set(editing)
                if (workId > -1) { //edit
                    w = dm.getWorkout(workId);
                } else { //adding a new workout
                    w = new Workout();
                    add = true;
                }
                EditText name = (EditText) findViewById(R.id.txtName);
                w.setName(name.getText().toString());
                //Sets
                EditText sets = (EditText) findViewById(R.id.txtSets);
                w.setSets(Integer.valueOf(sets.getText().toString()));
                //Reps
                EditText reps = (EditText) findViewById(R.id.txtReps);
                w.setReps(Integer.valueOf(reps.getText().toString()));
                //Weight
                EditText weight = (EditText) findViewById(R.id.txtWeightIn);
                w.setWeight(Float.valueOf(weight.getText().toString()));
                if (add) {
                    w.setDateStamp(dateStamp); //begining of  id # to be generated
                    dm.addWorkout(w, fireDir); //add the new workout and get it a new ID
                } else {
                    dm.editWorkout(fireDir);
                }

                finish();
            } else { //invalid entries alert user
                String fail; //place holder for where error occurred
                switch(valid) {
                    case -1:
                        fail = "Sets";
                        break;
                    case -2:
                        fail = "Reps";
                        break;
                    default:
                        fail = "Weight";
                        break;
                }
                Toast.makeText(getApplicationContext(), "Invalid input for " +
                                fail + ", please enter only digits and try again.",
                        Toast.LENGTH_SHORT).show();
            }
        } else { //fields were empty, alert user
            Toast.makeText(getApplicationContext(), "Please fill in all fields" +
                    " and resubmit.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Closes the activity and does nothing
     * @param view the Cancel Button
     */
    public void btnCancelOnClick(View view) {
        finish();
    }

    @Override
    public void setUpValueEventListeners() {
        fireDir = "worklist/" + userId;
        workListRef = FirebaseDatabase.getInstance().getReference(fireDir);
        if (workId >= 0) {
            if (workValueEventListener == null) {
                workValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<Workout>> t = new GenericTypeIndicator<List<Workout>>() {
                        };
                        List<Workout> workList = dataSnapshot.getValue(t);
                        if (workList == null) {
                            workList = new ArrayList<>();
                        }
                        dm.setWorkList(workList);
                        myWork = dm.getWorkout(workId);
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

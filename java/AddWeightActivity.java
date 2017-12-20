package leggettm.workoutlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
 * AddWeightActivity
 * Description Takes intent from a variety of other activities and determines whether
 * they are add or edit based on the parameters sent to it in the intent.
 *
 * @author Mel Leggett
 *         date 2/5/17
 */
public class AddWeightActivity extends CoreActivity {
    //class variables
    private DataManager dm;
    private int weightId;
    private Weight myWeight;
    private DatabaseReference weightListRef;
    private ValueEventListener weightValueEventListener;
    private String fireDir;
    private final String[] MONTHS = {"Month", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    //builds form
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spnMonth = (Spinner) findViewById(R.id.spnMonth);
        ArrayAdapter<String> lstAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, MONTHS);
        spnMonth.setAdapter(lstAdapter);

        //verify if add or edit dialog based on whether an ID was sent in intent
        Intent intent = getIntent();
        dm = DataManager.getDataManager();
        weightId = intent.getIntExtra(WeightDetailsActivity.EDIT_WEIGHT, -1);

        if (weightId >= 0) { //Edit type workout
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Weight");
            }
            myWeight = dm.getWeight(weightId);
            populateFields();

        }
    }

    private void populateFields() {
        //Display current fields to be edited
        //Month Spinner
        Spinner spnMonth = (Spinner) findViewById(R.id.spnMonth);
        spnMonth.setSelection(Integer.valueOf(
                Year.monthToDigit(myWeight.getMonth())));
        //Day
        EditText day = (EditText) findViewById(R.id.txtDay);
        day.setText(String.valueOf(myWeight.getDay()));
        //Weigh in
        EditText weight = (EditText) findViewById(R.id.txtWeightIn);
        weight.setText(String.valueOf(myWeight.getWeight()));
        //BMI
        EditText bmi = (EditText) findViewById(R.id.txtBmi);
        bmi.setText(String.valueOf(myWeight.getBmi()));
        //Systolic
        EditText sys = (EditText) findViewById(R.id.txtSys);
        sys.setText(String.valueOf(myWeight.getSystolic()));
        //Diastolic
        EditText dia = (EditText) findViewById(R.id.txtDia);
        dia.setText(String.valueOf(myWeight.getDiastolic()));
        //set focus
        spnMonth.requestFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (weightValueEventListener != null) {
            weightListRef.removeEventListener(weightValueEventListener);
        }
        weightValueEventListener = null;
    }

    public boolean nullCheck() {
        boolean check = true; //default to pass test
        //spinner month
        Spinner spin = (Spinner) findViewById(R.id.spnMonth);
        if (spin.getSelectedItemPosition() == 0)
            check = false;
        //Day field
        EditText day = (EditText) findViewById(R.id.txtDay);
        if (day.getText().toString().isEmpty())
            check = false;
        //Weight field
        EditText weight = (EditText) findViewById(R.id.txtWeightIn);
        if (weight.getText().toString().isEmpty()) {
            check = false;
            //change back color weight.setBackground();
        }//Bmi field
        EditText bmi = (EditText) findViewById(R.id.txtBmi);
        if (bmi.getText().toString().isEmpty()) {
            check = false;
        } //Systolic field
        EditText sys = (EditText) findViewById(R.id.txtSys);
        if (sys.getText().toString().isEmpty()) {
            check = false;
        }//Diastolic field
        EditText dia = (EditText) findViewById(R.id.txtDia);
        if (dia.getText().toString().isEmpty()) {
            check = false;
        }
        return check; //check passed
    }

    /**
     * @return int as test result, negative int = failed test
     */
    public int validateFields() {
        try {
            EditText day = (EditText) findViewById(R.id.txtDay);
            int test = Integer.valueOf(day.getText().toString());
            EditText weight = (EditText) findViewById(R.id.txtWeightIn);
            float test2 = Float.valueOf(weight.getText().toString());
            EditText bmi = (EditText) findViewById(R.id.txtBmi);
            test2 = Float.valueOf(bmi.getText().toString());
            EditText sys = (EditText) findViewById(R.id.txtSys);
            test = Integer.valueOf(sys.getText().toString());
            EditText dia = (EditText) findViewById(R.id.txtDia);
            test = Integer.valueOf(dia.getText().toString());
        } catch (NumberFormatException ex) {
            return -1;
        }
        return 0; //all validations passed, return pass: 0
    }

    /**
     * Saves the data used for either editing or saving
     *
     * @param view the Save button
     */
    public void btnSaveOnClick(View view) {
        if (nullCheck()) { //empty test passed, proceed
            int valid = validateFields();
            if (valid == 0) { //validate entry passed, proceed
                Weight w;
                boolean add = false;
                if (weightId > 0) { //edit, update existing entry
                    w = dm.getWeight(weightId);
                } else { //adding create new weight
                    w = new Weight();
                    add = true;
                }
                //set Month
                Spinner spin = (Spinner) findViewById(R.id.spnMonth);
                w.setMonth(String.valueOf(spin.getSelectedItem().toString()));
                //set day
                EditText day = (EditText) findViewById(R.id.txtDay);
                w.setDay(Integer.valueOf(day.getText().toString()));
                //set weight
                EditText weight = (EditText) findViewById(R.id.txtWeightIn);
                w.setWeight(Float.valueOf(weight.getText().toString()));
                //set BMI
                EditText bmi = (EditText) findViewById(R.id.txtBmi);
                w.setBmi(Float.valueOf(bmi.getText().toString()));
                //set systolic
                EditText sys = (EditText) findViewById(R.id.txtSys);
                w.setSystolic(Integer.valueOf(sys.getText().toString()));
                //set diastolic
                EditText dia = (EditText) findViewById(R.id.txtDia);
                w.setDiastolic(Integer.valueOf(dia.getText().toString()));
                //build placeholder
                w.setPlace(Year.monthToDigit(w.getMonth()), w.getDay());

                if (add) { //add new weight and assign a number
                    dm.addWeight(w, fireDir);
                } else {
                    w.setSorter(w.getPlace(), w.getWeightID());
                    dm.editWeight(fireDir);
                }
                finish();
            } else { //invalid entries alert user
                Toast.makeText(this, "Invalid input please enter" +
                                " only digits and try again.",
                        Toast.LENGTH_SHORT).show();
            }
        } else { //fields were empty, alert user
            Toast.makeText(this, "Please fill in all fields" +
                    " and resubmit.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Cancels adding/editing a weigh in and returns to previous form
     *
     * @param view the Cancel button
     */
    public void btnCancelOnClick(View view) {
        finish();
    }

    @Override
    public void setUpValueEventListeners() {
        fireDir = "weightlist/" + userId;
        weightListRef = FirebaseDatabase.getInstance().getReference(fireDir);
        if (weightId >= 0) {
            if (weightValueEventListener == null) {
                weightValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<Weight>> t = new GenericTypeIndicator<List<Weight>>() {
                        };
                        List<Weight> weightList = dataSnapshot.getValue(t);
                        if (weightList == null) {
                            weightList = new ArrayList<>();
                        }
                        dm.setWeightList(weightList);
                        myWeight = dm.getWeight(weightId);
                        populateFields();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("MYLOG", "Could not read Weigh in.", error.toException());
                    }
                };
            }
            weightListRef.addValueEventListener(weightValueEventListener);
        }
    }
}

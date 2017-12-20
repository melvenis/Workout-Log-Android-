package leggettm.workoutlog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

public class WeightDetailsActivity extends CoreActivity {
    //class instance variables
    private int weightId;
    private DataManager dm;
    private Weight myWeight;
    private DatabaseReference weightListRef;
    private ValueEventListener weightValueEventListener;
    public static final String EDIT_WEIGHT = "edit work"; //for Intent result
    private String fireDir;

    //builds form
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get selected weight
        dm = DataManager.getDataManager();
        //receive intent, if none found close page
        Intent intent = getIntent();
        weightId = intent.getIntExtra(WeightListActivity.WEIGHT_ID, -1);
        if (weightId < 0) { //no weight was selected, close
            finish();
        }
        populateFields(); //display weight details
    }

    public void onResume() {
        super.onResume();
        populateFields();
    }

    private void populateFields() {
        Weight myW = dm.getWeight(weightId);
        TextView date = (TextView) findViewById(R.id.lblDateOut);
        date.setText(myW.toString().substring(0, 10));
        TextView weight = (TextView) findViewById(R.id.lblWeightOut);
        weight.setText(myW.getWeight() + "lbs.");
        TextView bmi = (TextView) findViewById(R.id.lblBmi);
        bmi.setText(String.valueOf(myW.getBmi()));
        TextView blood = (TextView) findViewById(R.id.lblBlood);
        blood.setText(myW.getSystolic() + "/" + myW.getDiastolic());
    }

    public void editWeightOnClick(View view) {
        Intent intent = new Intent(this, AddWeightActivity.class);
        intent.putExtra(EDIT_WEIGHT, weightId);
        startActivity(intent);
    }

    public void deleteWeightOnClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Weighin")
                .setMessage("Delete This WeighIn?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dm.delWeight(weightId, fireDir);
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
    protected void onPause() {
        super.onPause();
        if (weightValueEventListener != null) {
            weightListRef.removeEventListener(weightValueEventListener);
        }
        weightValueEventListener = null;
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

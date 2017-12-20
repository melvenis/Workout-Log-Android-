package leggettm.workoutlog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightListActivity extends CoreActivity {
    //class variables
    private ListView lstWeight; //main list view on display
    ArrayAdapter<Weight> lstAdapter; //lstAdapter for linking the list view
    public static final String WEIGHT_ID = "weightId"; //final for intents
    List<Weight> weightList; //list of weigh in objects
    private DatabaseReference weightListRef;
    private ValueEventListener weightValueEventListener;
    DataManager dm; //DataManager instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up GUI components
        lstWeight = (ListView) findViewById(R.id.lstDates);
    }

    public void onResume() {
        super.onResume();
        //set up Weight list
        dm = DataManager.getDataManager();
        weightList = dm.getWeightList();
        Collections.sort(weightList);
        //set up list view adapter
        lstAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, weightList);
        lstWeight.setAdapter(lstAdapter);
        //create list view click event
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Weight myWeight = (Weight) lstWeight.getItemAtPosition(position);
                Intent intent = new Intent(WeightListActivity.this, WeightDetailsActivity.class);
                intent.putExtra(WEIGHT_ID, myWeight.getWeightID());
                startActivity(intent);
            }
        };
        lstWeight.setOnItemClickListener(itemClickListener);
    }

    public void btnGraphOnClick(View view) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.line_chart);
        dialog.setTitle("Weight Graph");

        Button okButton = (Button) dialog.findViewById(R.id.btnOk);
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        LineChart chart = (LineChart) dialog.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();
        for(Weight w: weightList) {
            entries.add(new Entry(w.getDay(), w.getWeight()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setLineWidth(18.02f);
        dataSet.setColor(55,101);
        dataSet.setValueTextColor(0);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();//refreshes chart
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (weightValueEventListener != null) {
            weightListRef.removeEventListener(weightValueEventListener);
        }
        weightValueEventListener = null;
    }

    public void btnAddWeightOnClick(View view) {
        Intent intent = new Intent(this, AddWeightActivity.class);
        startActivity(intent);
    }

    @Override
    public void setUpValueEventListeners() {

        weightListRef = FirebaseDatabase.getInstance().getReference("weightlist/" + userId);
        if(weightValueEventListener == null) {
            weightValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<Weight>> t = new GenericTypeIndicator<List<Weight>>() {
                    };
                    weightList = dataSnapshot.getValue(t);
                    if(weightList == null) {
                        weightList = new ArrayList<>();
                    }
                    dm.setWeightList(weightList);
                    lstAdapter.clear();
                    lstAdapter.addAll((weightList));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //value was not read
                    Toast.makeText(getBaseContext(), "It Failed!", Toast.LENGTH_SHORT).show();
                    Log.w("MYLOG", "Failed to read weight list.", databaseError.toException());
                }
            };
        }
        weightListRef.addValueEventListener(weightValueEventListener);
    }
}

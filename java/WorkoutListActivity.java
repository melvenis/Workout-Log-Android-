package leggettm.workoutlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkoutListActivity extends CoreActivity {
    public static final String WORKOUT_ID = "workout_id"; //key for sending intent
    public static final String WORKOUT_DATE_STAMP = "workout_date_stamp"; //key for new work
    private List<Workout> workList;
    private List<Workout> dayList;
    private ArrayAdapter<Workout> lstAdapter;
    private ListView lstDays;
    String keyBuilder = "17"; //String of date to search for
    private DatabaseReference workListRef;
    private ValueEventListener workValueEventListener;
    private DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list_activiy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //retrieve intent information
        Intent intent = getIntent();
        String day = intent.getStringExtra(DayListActivity.GET_DAY);
        String month = intent.getStringExtra(DayListActivity.GET_MONTH);
        keyBuilder = keyBuilder.concat(month).concat(day);

        //set up GUI components
        //grab the day that we are displaying and show in the main label
        TextView mainDate = (TextView)findViewById(R.id.lblDate);
        String today = Year.digitToAbbMonth(month).concat(" ");
        today = today.concat(day).concat("'s Workout Schedule:");
        mainDate.setText(today);
        //set up Workout list
        dm = DataManager.getDataManager();
        workList = dm.getWorkList();
        dayList = new ArrayList<>();
        lstDays = (ListView)findViewById(R.id.lstDays);
        refreshList(); //display any workouts in proper lists

        //create list view click event for upper
        AdapterView.OnItemClickListener upItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Workout myWork = (Workout) lstDays.getItemAtPosition(position);
                Intent intent = new Intent(WorkoutListActivity.this, WorkoutDetailsActivity.class);
                intent.putExtra(WORKOUT_ID, myWork.getWorkID());
                startActivity(intent);
            }
        };
        lstDays.setOnItemClickListener(upItemClickListener);
    }

    public void onResume() {
        super.onResume();
        lstAdapter.notifyDataSetChanged();
        refreshList();
    }

    /**
     * refreshList()
     * Refreshes the list of days that have workouts already planned
     * to display them in the Workout List View
     */
    private void refreshList() {
        //clears the arraylist and refreshes the list
        dayList.clear();
        //gets worklist with all workouts
        workList = dm.getWorkList();
        //searches all workouts to see if their dates occur on the current day
        for(int i = 0; i < workList.size(); i++) {
            if (workList.get(i).getDateStamp().contains(keyBuilder)) { //found relevant workout
                dayList.add(workList.get(i)); //add to dayList
            }
        }
        //set up list view adapter for upper
        lstAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dayList);
        //loads relevant workouts into the day list
        lstDays.setAdapter(lstAdapter);
    }

    /**
     * btnAddWOnClick
     * sends intent to the AddWorkoutActivity to add a new workout
     * @param view the addW button
     */
    public void btnAddWOnClick(View view) {
        Intent intent = new Intent(this, AddWorkoutActivity.class);
        intent.putExtra(WORKOUT_DATE_STAMP, keyBuilder); //serial building info based on date
        startActivity(intent);
    }

    @Override
    public void setUpValueEventListeners() {
        workListRef = FirebaseDatabase.getInstance().getReference("worklist/" + userId);
        if(workValueEventListener == null) {
            workValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<Workout>> t = new GenericTypeIndicator<List<Workout>>() {
                    };
                    workList = dataSnapshot.getValue(t);
                    if(workList == null) {
                        workList = new ArrayList<>();
                    }
                    dm.setWorkList(workList);
                    lstAdapter.clear();
                    refreshList();
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

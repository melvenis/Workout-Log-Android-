package leggettm.workoutlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Name DayListActivity
 * Description  Activity that lists all the workouts scheduled for the day and
 * allows the user to add/edit the workouts
 *
 * @author Mel Leggett
 * @version 1/13/2017
 */
public class DayListActivity extends CoreActivity {
    //class variables
    public static final String GET_DAY = "get day"; //for passing date chosen
    public static final String GET_MONTH = "get month"; //for passing month chosen
    public static int thisYear = 17;

    private String monthIndex;
    private Year year;
    private DataManager dm;;
    private List<Year> yearList;
    private List<String> thisMonth; //string array to hold the months data for the spinner
    private List<String> workoutDays; //List of all days being used
    private ListView lstWorkout; //list view of the dates
    //date format to display
    private SimpleDateFormat format = new SimpleDateFormat("MMM dd");
    private Date date;
    private String today; //instance variable to hold date in string format
    private DatabaseReference dateListRef;
    private ValueEventListener dateValueEventListener;
    ArrayAdapter<String> lstAdapter;
    ArrayAdapter<String> adapt;
    private String fireDir;

    //builds form
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //builds the form, GUI components
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //gets current date
        date = new Date();
        //string value of the date in MM/dd format
        today = format.format(date) + " Scheduled Workouts";
        //places date in Main date label
        TextView date = (TextView) findViewById(R.id.lblDate);
        date.setText(today);
    }

    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        //creates DataManager and Year Object
        dm = DataManager.getDataManager();
        yearList = dm.getYearList();
        workoutDays = new ArrayList<>();
        lstWorkout = (ListView) findViewById(R.id.lstWorkout);
        year = dm.getYear(thisYear);

        //finds array based on todays date
        monthIndex = Year.monthToDigit(today.substring(0,3));
        thisMonth = year.getMonth(monthIndex);
        TextView date = (TextView)findViewById(R.id.lblDate);
        date.setText(today);


        //set Display in Prev Next buttons
        Button pre = (Button)findViewById(R.id.btnPrev);
        Button next = (Button)findViewById(R.id.btnNext);
        //find the months based on the month displayed
        String borderMonths = Year.borderMonths(today.substring(0, 3));
        pre.setText("<<" + borderMonths.substring(0,3));
        next.setText(borderMonths.substring(4) + ">>");
        //fills Spinner with list of days
        Spinner spnDays = (Spinner) findViewById(R.id.spnDays);
        adapt = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, thisMonth);
        adapt.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spnDays.setAdapter(adapt);
        //refresh array list of dates that are built
        workoutDays.clear();
        for (int i = 1; i < thisMonth.size(); i++) {
            if (thisMonth.get(i).length() > 2) {
                String newDay = today.substring(0,3).concat(" ").
                        concat(thisMonth.get(i).substring(0,3));
                workoutDays.add(newDay);
            }
        }
        //set up GUI components

        //set up list view adapter
        lstAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, workoutDays);
        lstWorkout.setAdapter(lstAdapter);
        //create list view click event
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getDay = (String) lstWorkout.getItemAtPosition(position);
                getDay = getDay.substring(4,6);
                Intent intent = new Intent(DayListActivity.this, WorkoutListActivity.class);
                intent.putExtra(GET_DAY, getDay);
                intent.putExtra(GET_MONTH, monthIndex);
                startActivity(intent);
            }
        };
        lstWorkout.setOnItemClickListener(itemClickListener);
        lstAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Sends intent to AddWorkoutActivity with no id attached so a blank
     * workout will be available for the user to create
     *
     * @param view the add button
     */
    public void btnAddOnClick(View view) {
        Spinner spnDays = (Spinner) findViewById(R.id.spnDays);
        if (spnDays.getSelectedItemPosition() < 1) { //out of bounds warn user
            Toast.makeText(getApplicationContext(), "Select a day to add from the spinner",
                    Toast.LENGTH_SHORT).show();
        } else {
            String getDay = String.valueOf(spnDays.getSelectedItem()).substring(0,2);
            //note added workout in Array
            thisMonth.set(Integer.parseInt(getDay), getDay + " Already Exists Below");
            dm.editMonth(fireDir);
            Intent intent = new Intent(this, WorkoutListActivity.class);
            intent.putExtra(GET_DAY, getDay);
            intent.putExtra(GET_MONTH, monthIndex);
            startActivity(intent);
        }
    }

    public void btnPrevOnClick(View view) {
        Button button = (Button)findViewById(R.id.btnPrev);
        today = String.valueOf(button.getText()).substring(2);
        monthIndex = Year.monthToDigit(today);
        thisMonth = year.getMonth(monthIndex);
        today = today.concat(" Scheduled Workouts");
        refresh();
    }

    public void btnNextOnClick(View view) {
        Button button = (Button)findViewById(R.id.btnNext);
        today = String.valueOf(button.getText()).substring(0,3);
        monthIndex = Year.monthToDigit(today);
        thisMonth = year.getMonth(monthIndex);
        today = today.concat(" Scheduled Workouts");
        refresh();
    }

    public List<String> fillAdapter() {
        //refresh array list of dates that are built
        workoutDays.clear();
        for (int i = 1; i < thisMonth.size(); i++) {
            if (thisMonth.get(i).length() > 2) {
                String newDay = today.substring(0, 3).concat(" ").
                        concat(thisMonth.get(i).substring(0, 3));
                workoutDays.add(newDay);
            }
        }
        return workoutDays;
    }
    @Override
    public void setUpValueEventListeners() {
        fireDir = "monthlist/" + userId;
        dateListRef = FirebaseDatabase.getInstance().getReference(fireDir);
        if(dateValueEventListener == null) {
            dateValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<Year>> t = new GenericTypeIndicator<List<Year>>() {
                    };
                    yearList = dataSnapshot.getValue(t);
                    if(yearList == null) {
                        yearList = new ArrayList<>();
                    }
                    dm.setYearList(yearList);
                    lstAdapter.clear();
                    refresh();

                    }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //value was not read
                    Log.w("MYLOG", "Failed to read date list.", databaseError.toException());
                }
            };
        }
        dateListRef.addValueEventListener(dateValueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dateValueEventListener != null) {
            dateListRef.removeEventListener(dateValueEventListener);
        }
        dateValueEventListener = null;
    }
}

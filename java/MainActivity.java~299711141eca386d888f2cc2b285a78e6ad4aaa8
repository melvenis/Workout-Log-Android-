package leggettm.workoutlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * MainActivity
 * Shows the forms icon and directs to either Weigh Ins
 * or Workouts
 *
 * @author Mel Leggett
 *         date 02/12/17
 */
public class MainActivity extends CoreActivity {
    //builds form
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Directs to the Weigh ins form
     *
     * @param view the Weight button
     */
    public void btnWeighInsOnClick(View view) {
        Intent intent = new Intent(this, WeightListActivity.class);
        startActivity(intent);
    }

    /**
     * Directs to the Workouts form
     *
     * @param view the Workouts button
     */
    public void btnWorkOutsOnClick(View view) {
        Intent intent = new Intent(this, DayListActivity.class);
        startActivity(intent);
    }

    @Override
    public void setUpValueEventListeners() {

    }
}

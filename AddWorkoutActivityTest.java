package leggettm.workoutlog;

import android.widget.EditText;
import android.widget.RadioButton;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mellow on 2/18/2017.
 */
public class AddWorkoutActivityTest {
    DataManager dm;
    @Before
    public void setUp() throws Exception {
        dm = DataManager.getDataManager();
    }

    @Test
    public void btnSaveOnClick() throws Exception {
        Workout w;
        w = new Workout();
        w.setName("Test name");
        w.setDate("feb");
        w.setReps(30);
        w.setSets(10);
        w.setWeight(200.3f);
        dm.addWorkout(w);
        assertNotNull("not null failed", w);
        System.out.println("name: " + w.getName() + "\n"+ "\nSets: "
                + w.getSets() + "\n type in word: ");
    }

}
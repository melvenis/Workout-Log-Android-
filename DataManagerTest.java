package leggettm.workoutlog;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * tests all the functions of adding, removing and getting the Weight and Workout objects
 * Created by Mellow on 2/18/2017.
 */
public class DataManagerTest {
    DataManager dm;


    @Before //sets up a DataManager object prior to each test
    public void setUp() throws Exception {
        dm = DataManager.getDataManager();
    }

    //Workout list tests********************************************
    @Test //null check on getWorkList
    public void testgetWorkList() throws Exception {
        List<Workout> list = dm.getWorkList();
        assertNotNull("Worklist returned null", list);
    }


    @Test //null check on Get Workout
    public void testGetWorkout() throws Exception {
        Workout work = new Workout(0, "curls", 25.5f, 3, 10);
        dm.addWorkout(work);
        Workout testW = dm.getWorkout(work.getWorkID());
        assertNotNull("testGetWorkout Failed", testW);
    }

    @Test
    public void testGetInvalidWorkout() throws Exception {
        Workout testW = dm.getWorkout(999);
        assertNull("testGetInvalidWorkout failed", testW);
    }

    @Test //confirm adding valid workouts increases the list size
    public void testValidAddWorkout() throws Exception {
        int size1 = dm.getWorkList().size();
        Workout work = new Workout(0, "curls", 25.5f, 3, 10);
        dm.addWorkout(work);
        int size2 = dm.getWorkList().size();
        assertTrue("validAddWOrkout failed", size2 - size1 == 1);
    }

    @Test
    public void testInvalidAddWorkout() throws Exception {
        int size1 = dm.getWorkList().size();
        Workout work = null;
        dm.addWorkout(work);
        int size2 = dm.getWorkList().size();
        assertTrue("invalidAddWOrkout failed", size2 - size1 == 0);
    }

    @Test //confirm deleting workout decreases list size
    public void testDelWorkout() throws Exception {
        Workout work = new Workout(0, "curls", 25.5f, 3, 10);
        dm.addWorkout(work);
        int size1 = dm.getWorkList().size();
        dm.delWorkout(work.getWorkID());
        int size2 = dm.getWorkList().size();
        assertTrue("testDelWorkout failed", size1 - size2 == 1);
    }

    @Test
    public void testInvalidDelWorkout() throws Exception {
        Workout work = new Workout(0, "curls", 25.5f, 3, 10);
        dm.addWorkout(work);
        int size1 = dm.getWorkList().size();
        dm.delWorkout(999);
        int size2 = dm.getWorkList().size();
        assertTrue("testDelWorkout failed", size1 - size2 == 0);
    }

    //Weight list tests*********************************************
    @Test //null check on getWeightList
    public void testGetWeightList() throws Exception {
        List<Weight> weight = dm.getWeightList();
        assertNotNull("getWeightList failed", weight);
    }

    @Test //nullcheck on getValidWeight
    public void testGetValidWeight() throws Exception {
        Weight weight = new Weight(250f, 23.2f, 120, 110, 17);
        dm.addWeight(weight);
        Weight testW = dm.getWeight(weight.getWeightID());
        assertNotNull("getValidWeight failed", testW);
    }

    @Test
    public void testGetInvalidWeight() throws Exception {
        Weight weight = new Weight(250f, 23.2f, 120, 110, 17);
        dm.addWeight(weight);
        Weight testW = dm.getWeight(999);
        assertNull("getInValidWeight failed", testW);
    }
    @Test //confirms adding a weight increases the size of the WeightList
    public void testAddValidWeight() throws Exception {
        int size1 = dm.getWeightList().size();
        Weight weight = new Weight(250f, 23.2f, 120, 110, 17);
        dm.addWeight(weight);
        int size2 = dm.getWeightList().size();
        assertTrue("AddValidWeight failed", size2 - size1 == 1);
    }

    @Test
    public void testAddInvalidWeight() throws Exception {
        int size1 = dm.getWeightList().size();
        Weight weight = null;
        dm.addWeight(weight);
        int size2 = dm.getWeightList().size();
        assertTrue("AddInValidWeight failed", size2 - size1 == 0);
    }
    @Test //confirms deleting a weight decreases the size of the WeightList
    public void testDelValidWeight() throws Exception {
        Weight weight = new Weight(250f, 23.2f, 120, 110, 17);
        dm.addWeight(weight);
        int size1 = dm.getWeightList().size();
        dm.delWeight(weight.getWeightID());
        int size2 = dm.getWeightList().size();
        assertTrue("DelValidWeight failed", size1 - size2 == 1);
    }

    @Test
    public void testDelInvalidWeight() throws Exception {
        Weight weight = new Weight(250f, 23.2f, 120, 110, 17);
        dm.addWeight(weight);
        int size1 = dm.getWeightList().size();
        dm.delWeight(999);
        int size2 = dm.getWeightList().size();
        assertTrue("AddValidWeight failed", size1 - size2 == 0);
    }
}
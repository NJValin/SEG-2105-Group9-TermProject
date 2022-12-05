
package com.example.seg2105_termprojectcourseenrollmentapp;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Random;
import static org.junit.Assert.*;
import java.io.IOException;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    DBHelperForTest db;
    Context context;
    Random x;
    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = new DBHelperForTest(context);
        db.deleteAllCourses();
        x = new Random();
        db.deleteAllUsers();
    }
    @Test
    public void testRegisterAsStudent() {

        db = new DBHelperForTest(context);
        String username = "test"+x.nextInt(100);
        db.addUsers(username,"pass","student", "Hello", "world");
        assertEquals(true, db.userExists(username));
        db.deleteAllCourses();
    }

    @Test
    public void testEnroll(){
        db = new DBHelperForTest(context);
        assertFalse( db.enrol("testCourse","tess","testUser"));
        db.deleteAllUsers();
    }
    @Test
    public void testEnroll2() {
        db = new DBHelperForTest(context);
        String crsCode = "testCoursse"+x.nextInt(100);
        String username = "user"+x.nextInt(100);
        db.addCourse(crsCode, "test");
        db.addUsers(username, "pass", "student", "test", "test");
        db.enrol(crsCode, "test", username);
        assertEquals(1, db.getMyCoursesSize(username));
        db.deleteAllUsers();
        db.deleteAllCourses();
    }
    @Test
    public void testDropCourse() {
        db = new DBHelperForTest(context);
        String crsCode = "testCoursse"+x.nextInt(100);
        String username = "user"+x.nextInt(100);
        db.addCourse(crsCode, "test");
        db.addUsers(username, "pass", "student", "test", "test");
        db.enrol(crsCode, "test", username);
        db.dropClass(crsCode, username);
        assertEquals(0, db.getMyCoursesSize(username));
        db.deleteAllUsers();
        db.deleteAllCourses();
    }



    @After
    public void tearDown() {
        db.close();
    }
}
package com.example.seg2105_termprojectcourseenrollmentapp;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = new DBHelperForTest(context);
        db.deleteAllCourses();
        db.deleteAllUsers();
    }
    @Test
    public void testAddCourse() {
        db = new DBHelperForTest(context);
        for (int i =0; i<50;i++) {
            db.addCourse("crs"+i, "i="+i);
        }
        assertEquals(50, db.courseCount());
        db.deleteAllCourses();

    }
    @Test
    public void testDeleteCourse() {
        db = new DBHelperForTest(context);
        for (int x =1; x<10;x++) {
            db.addCourse("crs"+x, "i="+x);
        }
        int i = db.courseCount();
        db.removeCourse("crs"+i, "i="+i);
        assertEquals(i-1, db.courseCount());
        db.deleteAllCourses();
    }
    @Test
    public void testUserExists() {
        db = new DBHelperForTest(context);
        for (int i =0; i<50;i++) {
            db.addUsers("test"+i,"pass","student", "Hello", "world");
        }
        assertEquals(false, db.userExists("test50"));
        db.deleteAllUsers();
    }

    @Test
    public void testCheckLogin(){
        db = new DBHelperForTest(context);
        db.addUsers("test","pass","student", "Hello", "world");
        assertEquals(false, db.checkLogin("test1", "pass"));
        db.deleteAllUsers();
    }

    @Test
    public void testSetInstructor() {
        db = new DBHelperForTest(context);
        db.addCourse("testCourse", "test");
        assertEquals("N/A", db.getInstructor("testCourse", "test"));
        db.setInstructor("testCourse", "test", new String[] {"Justin", "Trudeau"});
        assertEquals("Justin Trudeau", db.getInstructor("testCourse", "test"));
        db.deleteAllCourses();
    }

    @After
    public void tearDown() {
        db.close();
    }


}
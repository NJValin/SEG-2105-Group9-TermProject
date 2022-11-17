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


    @After
    public void tearDown() {
        db.close();
    }


}
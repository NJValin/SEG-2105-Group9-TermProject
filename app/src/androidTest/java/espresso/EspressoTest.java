package espresso;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.seg2105_termprojectcourseenrollmentapp.CourseEnrollmentApp;
import com.example.seg2105_termprojectcourseenrollmentapp.DBHelper;
import com.example.seg2105_termprojectcourseenrollmentapp.LoginPage;
import com.example.seg2105_termprojectcourseenrollmentapp.StudentHomePage;
import com.example.seg2105_termprojectcourseenrollmentapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {
    @Rule
    public ActivityScenarioRule<StudentHomePage> activityRule =
            new ActivityScenarioRule<>(StudentHomePage.class);


    @Test
    public void courseCodeIsDisplayedAsHint() {
        onView(withHint("Course Code")).check(matches(isDisplayed()));
        onView(withHint("Course Code")).check(isCompletelyLeftOf(withId(R.id.crsCSearch)));
    }

    @Test
    public void courseNameIsDisplayedAsHint() {
        onView(withHint("Course Name")).check(matches(isDisplayed()));
        onView(withHint("Course Name")).check(isCompletelyLeftOf(withId(R.id.crsNSearch)));
    }

    @Test
    public void enrollIsCorrect(){
        String x = "ENG: 1102: Dr.N/A, Days:N/A @ N/A, N/A @ N/A student limit: 0\nN/A";
        onView(ViewMatchers.withId(R.id.crsCSearch)).perform(ViewActions.typeText("ENG"));
        onView(ViewMatchers.withId(R.id.crsNSearch)).perform(ViewActions.typeText("1102"));
        onData(hasToString(startsWith(x))).inAdapterView(withId(R.id.courseList)).atPosition(0).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.goToUsersClasses)).perform(ViewActions.click());
    }

    @Test
    public void searchIsCorrect(){
        onView(ViewMatchers.withId(R.id.crsCSearch)).perform(ViewActions.typeText("ENG"));
        onView(ViewMatchers.withId(R.id.crsNSearch)).perform(ViewActions.typeText("1102"));
        onView(withId(R.id.search)).perform(ViewActions.click());


    }

    @Test
    public void studentCourseIsDisplayed(){
        onView(withId(R.id.crsCSearch)).perform(ViewActions.typeText("ENG"));
        onView(withId(R.id.crsNSearch)).perform(ViewActions.typeText("1102"));
        onView(withId(R.id.courseList)).check((ViewAssertions.matches(Matchers.withListSize(1))));
        onView(withId(R.id.crsCSearch)).perform(ViewActions.clearText(), ViewActions.typeText("MAT"));
        onView(withId(R.id.crsNSearch)).perform(ViewActions.clearText(),ViewActions.typeText("1302"));
    }



}

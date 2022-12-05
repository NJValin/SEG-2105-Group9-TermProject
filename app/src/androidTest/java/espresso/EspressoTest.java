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
    public void searchIsCorrect(){
        onView(ViewMatchers.withId(R.id.crsCSearch)).perform(ViewActions.typeText("ENG"));
        onView(ViewMatchers.withId(R.id.crsNSearch)).perform(ViewActions.typeText("1102"));
        onView(withId(R.id.search)).perform(ViewActions.click());


    }

}

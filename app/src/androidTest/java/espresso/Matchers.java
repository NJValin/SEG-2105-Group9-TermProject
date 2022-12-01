package espresso;

import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {
    public static Matcher<View> withListSize(int size) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return ((ListView) item).getCount()==size;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("The course list should contain "+size+" courses");
            }
        };
    }
}

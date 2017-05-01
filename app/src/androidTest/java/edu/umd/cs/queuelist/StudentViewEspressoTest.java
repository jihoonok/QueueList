package edu.umd.cs.queuelist;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
/**
 * Created by jihoonok on 4/27/17.
 */

public class StudentViewEspressoTest extends BaseActivityEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void InstructorView() throws InterruptedException{

        // main page
        onView(withId(R.id.student)).perform(click());
        onView((withId(R.id.courseSpinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("CMSC131"))).perform(click());
        onView(withId(R.id.enterList)).perform(click());

        // Student Queue Page
        Thread.sleep(4000);
        onView(withId(R.id.myFAB)).perform(click());

        // Student info
        onView(withId(R.id.student_name)).perform(typeText("Clyde Kruskal"));
        onView(withId(R.id.user_cell)).perform(typeText("1234567890"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.spinner_class)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("CMSC131"))).perform(click());

        onView(withId(R.id.spinner_assignment)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Project 3"))).perform(click());

        onView(withId(R.id.problem)).perform(typeText("Can you make N = 1 to make P = NP"));

        onView(withId(R.id.submit)).perform(click());

    }



    @Override
    public Activity getActivity() {
        return (Activity) activityRule.getActivity();
    }
}

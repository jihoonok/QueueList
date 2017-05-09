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

public class InstructorViewEspressoTest extends BaseActivityEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void InstructorView() throws InterruptedException{
        onView(withId(R.id.instructor)).perform(click());

        // Instructor Login Page
        onView(withId(R.id.username)).perform(typeText("CMSC131"));
        onView(withId(R.id.password)).perform(typeText("sprcoredump"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.spinner_class)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("CMSC131"))).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.submit)).perform(click()); // Enter credentials


        // Instructor View Page
        Thread.sleep(2000);

        // add project
        onView(withId(R.id.addProject)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.projectName)).perform(typeText("Project 7"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_project)).perform(click());

        // next student
        onView(withId(R.id.next)).perform(click());
        Thread.sleep(4000);
        onView(withId(R.id.viewQueue)).perform(click());
        Thread.sleep(4000);
        Espresso.pressBack();
        Thread.sleep(4000);
        // add student back to queue
        onView(withId(R.id.add_student)).perform(click());
        Thread.sleep(4000);
        //onView(withId(R.id.viewQueue)).perform(click());
        // View the queue
        onView(withId(R.id.viewQueue)).perform(click());

    }



    @Override
    public Activity getActivity() {
        return (Activity) activityRule.getActivity();
    }
}

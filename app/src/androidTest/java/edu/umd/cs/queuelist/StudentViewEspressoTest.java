package edu.umd.cs.queuelist;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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
    public void StudentView() throws InterruptedException{

        // main page
        onView(withId(R.id.student)).perform(click());
        Thread.sleep(2000);
        onView((withId(R.id.courseSpinner))).perform(click());
        Thread.sleep(2000);
        onData(allOf(is(instanceOf(String.class)), is("CMSC132"))).perform(click());
        onView(withId(R.id.enterList)).perform(click());

        // test the swipe refresh
        Thread.sleep(4000);
        onView(withId(R.id.swipeRefreshLayout))
                .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));

        // View the student's entry
        Thread.sleep(2000);
        onView(withText("Charles")).perform(click());

        // go back to the main page
        Thread.sleep(2000);
        Espresso.pressBack();
        Espresso.pressBack();

        // go to the 131 list
        onView(withId(R.id.student)).perform(click());
        Thread.sleep(2000);
        onView((withId(R.id.courseSpinner))).perform(click());
        Thread.sleep(2000);
        onData(allOf(is(instanceOf(String.class)), is ("CMSC131"))).perform(click());
        onView(withId(R.id.enterList)).perform(click());


        // Student Queue Page
        Thread.sleep(4000);
        onView(withId(R.id.fab)).perform(click());

        // Student info
        onView(withId(R.id.student_name)).perform(typeText("Clyde Kruskal"));
        Thread.sleep(2000);
        onView(withId(R.id.user_cell)).perform(typeText("1234567890"));
        Thread.sleep(2000);
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.spinner_class)).perform(click());
        Thread.sleep(2000);
        onData(allOf(is(instanceOf(String.class)), is("CMSC131"))).perform(click());

        onView(withId(R.id.spinner_assignment)).perform(click());
        Thread.sleep(2000);
        onData(allOf(is(instanceOf(String.class)), is("Project 3"))).perform(click());

        Thread.sleep(2000);
        onView(withId(R.id.problem)).perform(typeText("Hello Charles I need help"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.check_in_button)).perform(click());
        Thread.sleep(5000);
    }

    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }
    @Override
    public Activity getActivity() {
        return (Activity) activityRule.getActivity();
    }
}

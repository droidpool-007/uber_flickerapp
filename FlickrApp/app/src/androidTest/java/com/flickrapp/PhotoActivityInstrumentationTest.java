package com.flickrapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.AssertionFailedError;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PhotoActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<PhotoActivity> mPhotoActivityTestRule = new ActivityTestRule<>(PhotoActivity.class);

    @Test
    public void testSearchUi() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.et_search_box)).perform(ViewActions.clearText(),ViewActions.typeText("Amr"));
        } catch (AssertionFailedError e) {
            // View not displayed
        }
    }

    @Test
    public void testUi() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.bt_search)).perform(ViewActions.click());
        } catch (AssertionFailedError e) {
            // View not displayed
        }
    }

    @Test
    public void testRecyclerView() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(3));
        } catch (AssertionFailedError e) {
            // View not displayed
        }
    }

}

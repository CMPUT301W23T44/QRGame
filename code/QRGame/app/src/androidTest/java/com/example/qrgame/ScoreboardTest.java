package com.example.qrgame;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

// Made by Alex Huo
// This is the scoreboard test to test buttons and display the right activity

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScoreboardTest {

    @Before
    public void launchActivity() {
        ActivityScenario.launch(Scoreboard.class);
    }

    @Test
    public void verifyScoreboardElementsAreDisplayed() {
        // Check if the scoreboard ListView is displayed
        Espresso.onView(withId(R.id.scoreboard_list))
                .check(matches(isDisplayed()));

        // Check if the current user rank TextView is displayed
        Espresso.onView(withId(R.id.cur_rank))
                .check(matches(isDisplayed()));

        // Check if the current user score TextView is displayed
        Espresso.onView(withId(R.id.cur_score))
                .check(matches(isDisplayed()));
    }

    @Test
    public void verifyButtonsAreDisplayed() {
        // Check if the "Return" button is displayed
        Espresso.onView(withId(R.id.button2))
                .check(matches(isDisplayed()))
                .check(matches(withText("Return")));

        // Check if the "SearchUser" button is displayed
        Espresso.onView(withId(R.id.button4))
                .check(matches(isDisplayed()))
                .check(matches(withText("Search User")));

        // Check if the "SearchQR" button is displayed
        Espresso.onView(withId(R.id.button3))
                .check(matches(isDisplayed()))
                .check(matches(withText("Search QR")));
    }

    @Test
    public void verifyButtonClicks() {
        // Click on the "SearchUser" button and verify if the SearchUser activity is displayed
        Espresso.onView(withId(R.id.button4)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.searchuser))
                .check(matches(isDisplayed()));

        // Go back to the Scoreboard activity
        Espresso.pressBack();

        // Click on the "SearchQR" button and verify if the SearchQR activity is displayed
        Espresso.onView(withId(R.id.button3)).perform(click());
        Espresso.onView(withId(R.id.searchqr_searchqr))
                .check(matches(isDisplayed()));

        // Go back to the Scoreboard activity
        Espresso.pressBack();
    }
}

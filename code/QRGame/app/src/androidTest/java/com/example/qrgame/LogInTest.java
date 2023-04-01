package com.example.qrgame;


import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LogInTest {


    Solo solo;

    @Rule
    public ActivityTestRule<MainPageActivity> rule =
            new ActivityTestRule<>(MainPageActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testLogoutAndLogin() {
        // Ensure the app is in the correct starting activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);

        // Click the logout button to open the login activity
        solo.clickOnView(solo.getView(R.id.logout_button));

        // Wait for the login activity to open
        solo.waitForActivity(LogIn.class);

        // Enter username and phone number
        solo.enterText((EditText) solo.getView(R.id.username), "test1234");
        solo.enterText((EditText) solo.getView(R.id.number), "9876543210");

        // Click the login button
        solo.clickOnView(solo.getView(R.id.login_button));

        // Wait for the MainPageActivity to open after login
        assertTrue(solo.waitForActivity(MainPageActivity.class, 5000));

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}

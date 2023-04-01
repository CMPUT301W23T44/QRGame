package com.example.qrgame;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;
import java.util.Random;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SignUpTest {







    Solo solo;

    @Rule
    public ActivityTestRule<MainPageActivity> rule =
            new ActivityTestRule<>(MainPageActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testSignUpButton() {
        // Ensure the app is in the correct starting activity
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);

        // Click the logout button to open the login activity
        solo.clickOnView(solo.getView(R.id.logout_button));

        // Wait for the login activity to open
        solo.waitForActivity(LogIn.class);

        // Click the sign up button
        solo.clickOnView(solo.getView(R.id.signup_button));

        // Wait for the SignUp activity to open
        assertTrue(solo.waitForActivity(SignUp.class, 5000));

        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Define the letters to choose from
        Random random = new Random(); // Create a new random number generator

        StringBuilder sb = new StringBuilder(); // Create a new StringBuilder object to store the generated string
        int length = random.nextInt(7) + 1; //

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(letters.length()); // Generate a random index to choose a letter from the letters string
            char c = letters.charAt(index); // Get the character at the random index
            sb.append(c); // Add the character to the StringBuilder
        }

        String randomString = sb.toString();

        // Enter username and password
        solo.enterText((EditText) solo.getView(R.id.Username), randomString);
        solo.enterText((EditText) solo.getView(R.id.PhoneNumber), "1234543210");

        // Click the finish button
        solo.clickOnView(solo.getView(R.id.Finish_button));

        // Wait for the MainPageActivity to open after login
        assertTrue(solo.waitForActivity(MainPageActivity.class, 5000));


    }
}

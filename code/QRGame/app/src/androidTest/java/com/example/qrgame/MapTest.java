package com.example.qrgame;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.qrgame.ToastMatcher;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RunWith(AndroidJUnit4.class)
public class MapTest {

    private FirebaseFirestore firebaseDatabase = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseDatabase.collection("qr_codes");
    private QuerySnapshot querySnapshot;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testMapDialog() {
        // Click on the "Search QRCode" button
        Espresso.onView(ViewMatchers.withId(R.id.search)).perform(ViewActions.click());

        // Type in the name of the QRCode to search for
        Espresso.onView(ViewMatchers.withId(R.id.latitude)).perform(ViewActions.typeText("Test QRCode"), ViewActions.closeSoftKeyboard());

        // Click on the "Search" button
        Espresso.onView(ViewMatchers.withText("Search")).perform(ViewActions.click());

        // Wait for 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that the expected toast message is displayed
        Espresso.onView(withText("Test QRCode is not found "))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));


    }
}

package com.example.qrgame;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.qrgame.LatlngSearch;
import com.example.qrgame.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LatlngSearchTest {

    private FirebaseFirestore firebaseDatabase = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseDatabase.collection("qr_codes");
    private QuerySnapshot querySnapshot;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testSearchQRCodeByLatlng() {
        // Click on the "Search QRCode" button
        Espresso.onView(ViewMatchers.withId(R.id.searchlatLng)).perform(ViewActions.click());


        // Enter latitude and longitude values
        Espresso.onView(ViewMatchers.withId(R.id.latitude)).perform(ViewActions.typeText("12.9716"));
        Espresso.onView(ViewMatchers.withId(R.id.longitude)).perform(ViewActions.typeText("77.5946"));

        // Click on the search button
        Espresso.onView(ViewMatchers.withText("Search")).perform(ViewActions.click());

        // Verify that the count of QR codes near the entered location is displayed
        Espresso.onView(ViewMatchers.withText("There are 32 Qrcodes near this location"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}


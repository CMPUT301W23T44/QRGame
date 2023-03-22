package com.example.qrgame;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class QRScannerActivityTest {

    private Solo solo;



    @Rule
    public ActivityTestRule<MainPageActivity> rule =
            new ActivityTestRule<>(MainPageActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testQRScannerButton() {
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_qr));
        solo.assertCurrentActivity("Wrong Activity", QRScannerActivity.class);
    }

    
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}

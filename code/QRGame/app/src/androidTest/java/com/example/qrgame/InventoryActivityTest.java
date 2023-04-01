package com.example.qrgame;

import android.widget.Button;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class InventoryActivityTest {
    Solo solo;

    @Rule
    public ActivityTestRule<MainPageActivity> rule =
            new ActivityTestRule<>(MainPageActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


    @Test
    public void InventoryButton() {
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnView((Button) solo.getView(R.id.inventory_button));
        solo.assertCurrentActivity("Wrong Activity", Inventory_activity.class);
    }


    @Test
    public void InventoryBackButton() {
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.clickOnView((Button) solo.getView(R.id.inventory_button));
        solo.assertCurrentActivity("Wrong Activity", Inventory_activity.class);
        solo.clickOnView((Button) solo.getView(R.id.inventory_back_button));
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
    }




    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

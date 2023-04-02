package com.example.qrgame;

import android.widget.Button;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DetailsActivityTest {

    Solo solo;

    @Rule
    public ActivityTestRule<Inventory_activity> rule =
            new ActivityTestRule<>(Inventory_activity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }



    @Test
    public void DetailsFinishButton() {
        solo.assertCurrentActivity("Wrong Activity", Inventory_activity.class);
        solo.clickInList(1);
        solo.assertCurrentActivity("Wrong Activity", DetailsActivity.class);
        solo.clickOnView((Button) solo.getView(R.id.finish_button));
        solo.assertCurrentActivity("Wrong Activity", Inventory_activity.class);
    }




    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}

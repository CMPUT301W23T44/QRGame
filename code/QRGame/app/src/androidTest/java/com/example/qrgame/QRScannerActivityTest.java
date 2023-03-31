package com.example.qrgame;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
        // Click on Add QR code button and check if on the right screen
        solo.clickOnView(solo.getView(R.id.add_qr));
        solo.assertCurrentActivity("Wrong Activity", QRScannerActivity.class);

        // Press the back button and check if the error screen works
        solo.clickOnView(solo.getView(R.id.finish_button));
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.waitForActivity(ExistingQRInfoActivity.class, 1000);
    }

    @Test
    public void testQRExistsLine() {
        // Create mock QR code object
        QRCode qrCode = TestingMode.MockExistingQRCode();
        QRDatabaseController instance = QRDatabaseController.getInstance();
        instance.pushQR(qrCode);

        // Allows mock objects to be used
        TestingMode.testingMode = true;

        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        // Move to Add QR code screen
        solo.clickOnView(solo.getView(R.id.add_qr));
        solo.assertCurrentActivity("Wrong Activity", QRScannerActivity.class);

        solo.clickOnView(solo.getView(R.id.finish_button));
        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);
        solo.waitForActivity(ExistingQRInfoActivity.class, 1000);

        // Make sure values were set properly
        solo.searchText(qrCode.getFace());
        solo.searchText(String.valueOf(qrCode.getScore()));
        solo.searchText(qrCode.getName());
        String comment = qrCode.getComments().get(TestingMode.testUser);
        solo.searchText(comment);

        // Assure that the back button takes user to main page
        solo.clickOnView(solo.getView(R.id.finish_button));
        solo.waitForActivity(MainPageActivity.class, 1000);

        // Remove user from QR code database
        instance.deleteQR(qrCode.getHash());
    }

    @Test
    public void testNewQRLine() {
        // Create mock QR code object and delete it if it exists
        QRCode qrCode = TestingMode.MockExistingQRCode();
        QRDatabaseController instance = QRDatabaseController.getInstance();
        instance.deleteQR(qrCode.getHash());

        // Allows mock objects to be used
        TestingMode.testingMode = true;

        solo.assertCurrentActivity("Wrong Activity", MainPageActivity.class);

        // Move to Add QR code screen
        solo.clickOnView(solo.getView(R.id.add_qr));
        solo.assertCurrentActivity("Wrong Activity", QRScannerActivity.class);

        solo.clickOnView(solo.getView(R.id.finish_button));
        solo.waitForActivity(NewQRInfoActivity.class, 1000);

        // Make sure values were set properly
        solo.searchText(qrCode.getFace());
        solo.searchText(String.valueOf(qrCode.getScore()));
        solo.searchText(qrCode.getName());
        solo.enterText((EditText) solo.getView(R.id.editText_comment) , "Testing");

        // Move to camera screen
        solo.clickOnView(solo.getView(R.id.finish_button));
        solo.waitForActivity(PromptUserPictureActivity.class, 1000);

        // Assure camera opens properly
        solo.clickOnView(solo.getView(R.id.capture_button));
        solo.waitForActivity(SaveSurroundingPictureActivity.class, 1000);

        // Press back and retry taking a picture
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.waitForActivity(PromptUserPictureActivity.class, 1000);

        // Attempt to recapture an image
        solo.clickOnView(solo.getView(R.id.capture_button));
        solo.waitForActivity(SaveSurroundingPictureActivity.class, 1000);

        // Finish camera and move back to main page
        solo.clickOnView(solo.getView(R.id.finish_button));
        solo.waitForActivity(MainPageActivity.class, 1000);

        // Delete created QR code from database
        instance.deleteQR(qrCode.getHash());
    }
    
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }


}

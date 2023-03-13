package com.example.qrgame;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import android.widget.Button;
import org.junit.jupiter.api.Test;
import com.google.android.gms.maps.model.LatLng;
import org.junit.After;
import org.junit.Rule;
import java.util.ArrayList;

//@RunWith(AndroidJUnit4.class)
public class MainPageActivityTest {

    private MainPageActivity mainPageActivity;

    @After
    public void tearDown() {
        mainPageActivity = null;
    }

    @Test
    public void testLatLng() {
        LatLng sydney = new LatLng(-34, 151);
        assertNotNull(sydney);
    }



    @Test
    public void testLocationArrayList() {
        ArrayList<LatLng> locationArrayList = new ArrayList<>();
        locationArrayList.add(new LatLng(-34, 151));
        locationArrayList.add(new LatLng(-31.083332, 150.916672));
        locationArrayList.add(new LatLng(-32.916668, 151.750000));
        locationArrayList.add(new LatLng(-27.470125, 153.021072));
        locationArrayList.add(new LatLng(54, -114));
        locationArrayList.add(new LatLng(55, -115));
        locationArrayList.add(new LatLng(56, -114));
        locationArrayList.add(new LatLng(57, -115));
        assertNotNull(locationArrayList);
    }

    @Test
    public void testAddQrButton() {
        Button addQrButton = mainPageActivity.findViewById(R.id.add_qr);
        assertNotNull(addQrButton);
        addQrButton.performClick();
    }

    @Test
    public void testLogoutButton() {
        Button logoutButton = mainPageActivity.findViewById(R.id.logout_button);
        assertNotNull(logoutButton);
        logoutButton.performClick();
    }
}

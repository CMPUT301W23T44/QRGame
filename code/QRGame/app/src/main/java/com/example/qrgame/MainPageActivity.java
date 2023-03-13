package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MainPageActivity extends AppCompatActivity {

    private FloatingActionButton addQr_button;
    private Button inventory_button;
    private Button social_button;
    private Button logout_button;

    private final int QR_SCANNER_REQUEST = 0;
    private final int INVENTORY_REQUEST = 1;
    private final int SOCIAL_REQUEST = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        logout_button = findViewById(R.id.logout_button);
<<<<<<< HEAD
        search_button = findViewById(R.id.search);
//        search_button.setOnClickListener(v -> {
//            new Map().show(getSupportFragmentManager(), "Search QRCode");
//        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mMap);
        mapFragment.getMapAsync(this);
=======
>>>>>>> 178b06d82055755a25d3fa2171d7446a367d1db5
        Intent login_page = new Intent(this, LogIn.class);
        String deviceId= getUdid();

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

                fireStore.collection("LoginUser").document(deviceId).delete();
                startActivity(login_page);
            }
        });
        addQr_button = findViewById(R.id.add_qr);

        Intent qr_scanner = new Intent(this, QRScannerActivity.class);
        // Player chose to add a QR code
        addQr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivityForResult(qr_scanner, QR_SCANNER_REQUEST);
            }
        });

        inventory_button = findViewById(R.id.inventory_button);
        Intent inventory =new Intent(MainPageActivity.this,Inventory_activity.class);
        inventory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(inventory);
            }
        });

        social_button = findViewById(R.id.social_button);

        social_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == QR_SCANNER_REQUEST) {
                try {
                    String result = null;
                    if (data != null) {
                        result = data.getStringExtra("result");
                    } else {
                        result = "00000000";    // TODO - Change with a zero score QR code
                    }
                    // Create a new QR code and add it to the database
                    QRCode qrCode = new QRCode(result);

                    QRDatabaseController qrDB = new QRDatabaseController();
                    qrDB.addQR(qrCode);
                    // Display the QR codes info
                    Intent qrInfoIntent = new Intent(this, QRInfoActivity.class);
                    qrInfoIntent.putExtra("qrCode", qrCode);
                    startActivity(qrInfoIntent);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    /**
     * return the AndroidID
     * @return
     */
    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }

    /**
     * return the deviceId
     * @return
     */
    public String getUdid() {
        String androidID=AndroidID();
        return"2"+ UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-","");
    }
}

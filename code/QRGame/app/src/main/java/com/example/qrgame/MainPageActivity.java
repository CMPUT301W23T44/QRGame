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

    final int QR_SCANNER_REQUEST = 0;
    final int INVENTORY_REQUEST = 1;
    final int SOCIAL_REQUEST = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        logout_button = findViewById(R.id.logout_button);
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
        addQr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivityForResult(qr_scanner, QR_SCANNER_REQUEST);
            }
        });

        inventory_button = findViewById(R.id.inventory_button);

        inventory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    String result = data.getStringExtra("result");
                    QRCode qrCode = new QRCode(result);
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

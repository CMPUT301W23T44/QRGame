package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.NoSuchAlgorithmException;

public class MainPageActivity extends AppCompatActivity {

    private FloatingActionButton addQr_button;
    private Button inventory_button;
    private Button social_button;

    final int QR_SCANNER_REQUEST = 0;
    final int INVENTORY_REQUEST = 1;
    final int SOCIAL_REQUEST = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

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
                Intent intent=new Intent(MainPageActivity.this,Inventory_activity.class);
                startActivity(intent);

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
                    QRDatabaseController.addQR(qrCode);
                    boolean test = QRDatabaseController.findQR(qrCode.getHash());
                    QRCode qrCode2 = QRDatabaseController.getQRCode(qrCode.getHash());
                    Intent qrInfoIntent = new Intent(this, QRInfoActivity.class);
                    qrInfoIntent.putExtra("qrCode", qrCode);
                    startActivity(qrInfoIntent);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainPageActivity extends AppCompatActivity {

    FloatingActionButton add_qr_button;
    Button inventory_button;
    Button social_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        add_qr_button = findViewById(R.id.add_qr);

        Intent qr_scanner = new Intent(this, QRScannerActivity.class);
        add_qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(qr_scanner);
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
}

package com.example.qrgame;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Prompts user to take a picture of the location they are in
 */
public class PromptUserPictureActivity extends AppCompatActivity {

    ImageView imageView;
    Button captureButton;
    private final static int REQUEST_CODE = 0;
    private final static int RESULT_NEXT = 1;
    private final static int RESULT_BACK = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        imageView = findViewById(R.id.imageview);
        captureButton = findViewById(R.id.capture_button);


        captureButton.setOnClickListener(view -> {
            Intent savePicture = new Intent(this, SaveSurroundingPictureActivity.class);
            startActivityForResult(savePicture, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the user was happy with the picture, the system returns to the main page
        if (resultCode == RESULT_NEXT) {
            finish();
        // If the user does not like the picture, another one can be taken
        } else {
            Intent savePicture = new Intent(this, SaveSurroundingPictureActivity.class);
            startActivityForResult(savePicture, REQUEST_CODE);
        }
    }
}
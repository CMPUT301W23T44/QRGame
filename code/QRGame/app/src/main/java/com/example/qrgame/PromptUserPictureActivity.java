package com.example.qrgame;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Prompts user to take a picture of the location they are in
 */
public class PromptUserPictureActivity extends AppCompatActivity {

    Button captureButton;
    private final static int REQUEST_CODE = 0;
    private final static int RESULT_NEXT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

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
            assert data != null;
            byte[] byteArray = (byte[]) data.getExtras().get("bytes");
            Intent previous = new Intent();
            previous.putExtra("bytes", byteArray);
            setResult(RESULT_OK, previous);
            finish();
        // If the user does not like the picture, another one can be taken
        } else {
            Intent savePicture = new Intent(this, SaveSurroundingPictureActivity.class);
            startActivityForResult(savePicture, REQUEST_CODE);
        }
    }
}
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

import java.io.ByteArrayOutputStream;

/**
 * Opens a camera and allows the user to take a picture
 */
public class SaveSurroundingPictureActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 1;
    private final static int RESULT_NEXT = 1;
    private final static int RESULT_BACK = -1;

    ImageView imageView;
    Button nextButton;
    Button backButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_surrounding_picture_activity);

        imageView = findViewById(R.id.imageview);
        nextButton = findViewById(R.id.details_back_button);
        backButton = findViewById(R.id.back_button);

        // Request permission to use the camera if permission is not already granted
        if (ContextCompat.checkSelfPermission(SaveSurroundingPictureActivity.this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SaveSurroundingPictureActivity.this, new String[] {
                    Manifest.permission.CAMERA
            }, REQUEST_CODE);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);

        nextButton.setOnClickListener(view -> finish());

        backButton.setOnClickListener(view -> {
            setResult(RESULT_BACK, null);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the user is happy with the picture, it is stored to the QR code
        if (requestCode == REQUEST_CODE) {
            assert data != null;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

            // Compresses the image into a png
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent previous = new Intent();
            previous.putExtra("bytes", byteArray);
            setResult(RESULT_NEXT, previous);
        }
    }
}

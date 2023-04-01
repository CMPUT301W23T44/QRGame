package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Base64;

/**
 * Displays all the information of a new QR code to the user
 */
public class NewQRInfoActivity extends AppCompatActivity {

    private TextView qrImageTextView;
    private TextView nameTextView;
    private TextView scoreTextView;
    private EditText commentEditText;
    private QRCode qrCode;
    private Button nextButton;
    private CheckBox locationCheckbox;

    private final int PICTURE_REQUEST = 1;

    private String comment;
    private String userName;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_qr_info_page);

        qrImageTextView = findViewById(R.id.details_qr_face);
        nameTextView = findViewById(R.id.details_name);
        scoreTextView = findViewById(R.id.details_score);
        nextButton = findViewById(R.id.finish_button);
        commentEditText = findViewById(R.id.editText_comment);
        locationCheckbox = findViewById(R.id.checkBox_geolocation);

        // Gets new QR code and current user logged in
        qrCode = (QRCode) getIntent().getSerializableExtra("qrCode");
        userName = getIntent().getStringExtra("Username");

        if (userName == null) {
            userName = " ";
        }

        // Sets info to be displayed onto the screen
        qrImageTextView.setText(qrCode.getFace());
        nameTextView.setText(qrCode.getName());
        scoreTextView.setText("Score: " + qrCode.getScore());

        // Creates intent to move to take a new picture
        Intent surroundingsPictureActivity = new Intent(this, PromptUserPictureActivity.class);

        nextButton.setOnClickListener(view -> {
            // Saves the current comment and the location if the user chooses to
            comment = String.valueOf(commentEditText.getText());
            if (locationCheckbox.isChecked()) {
                longitude = getIntent().getDoubleExtra("longitude", 0);
                latitude = getIntent().getDoubleExtra("latitude", 0);
                qrCode.setLatLong(latitude, longitude);
            }
            startActivityForResult(surroundingsPictureActivity, PICTURE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If a picture was properly received, it is saved as a string
        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            byte[] bytes = (byte[]) data.getExtras().get("bytes");
            // Encodes a byte area into a string
            qrCode.setLocation_image(Base64.getEncoder().encodeToString(bytes));
        }
        // Adds the comment to the QR code and then push the QR code to database
        qrCode.addComments(userName, comment);
        QRDatabaseController dbAdapter = QRDatabaseController.getInstance();
        dbAdapter.pushQR(qrCode);
        User.addQRCode(qrCode, userName);
        finish();
    }
}

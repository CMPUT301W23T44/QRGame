package com.example.qrgame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

/**
 * Displays all the information of an existing QR code to the user
 */
public class ExistingQRInfoActivity extends AppCompatActivity {
    private TextView qrImageTextView;
    private TextView nameTextView;
    private TextView scoreTextView;
    private TextView tag;
    private EditText commentEditText;
    private QRCode qrCode;
    private Button nextButton;
    private boolean scanned;
    private String userName;

    private final int PICTURE_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_qr_info_page);

        qrImageTextView = findViewById(R.id.details_qr_face);
        nameTextView = findViewById(R.id.details_name);
        scoreTextView = findViewById(R.id.details_score);
        nextButton = findViewById(R.id.finish_button);
        commentEditText = findViewById(R.id.editText_comment);
        tag = findViewById(R.id.details_qr_name);

        // Gets the QR, the current logged in user, and if the QR code has been scanned already
        qrCode = (QRCode) getIntent().getSerializableExtra("qrCode");
        scanned = getIntent().getBooleanExtra("scanned", false);
        userName = getIntent().getStringExtra("Username");

        if (userName == null) {
            userName = " ";
        }

        // Sets the info to be displayed to the user on the screen
        qrImageTextView.setText(qrCode.getFace());
        nameTextView.setText(qrCode.getName());
        scoreTextView.setText("Score: " + qrCode.getScore());
        HashMap<String, String> commentsMap = qrCode.getComments();

        // If the QR code has already been scanned, the user is notified and the comment
        // they had previously entered is displayed
        if (scanned) {
            tag.setText("Already Owned!");
            commentEditText.setText(commentsMap.get(userName));
            commentEditText.setEnabled(false);
            nextButton.setText("Finish");
        } else {
            tag.setText("New QR Code!");
        }

        nextButton.setOnClickListener(view -> {
            if (!scanned) {
                // If not already scanned by the user, a comment can be added
                String comment = String.valueOf(commentEditText.getText());
                qrCode.addComments(userName, comment);
                QRDatabaseController dbAdapter = QRDatabaseController.getInstance();
                dbAdapter.pushQR(qrCode);
                User.addQRCode(qrCode, userName);
            }
            finish();
        });
    }
}

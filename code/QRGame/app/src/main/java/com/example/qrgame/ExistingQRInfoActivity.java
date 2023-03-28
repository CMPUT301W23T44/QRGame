package com.example.qrgame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

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
    private ImageView surroundingImageView;

    private final int PICTURE_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_qr_info_page);

        qrImageTextView = findViewById(R.id.textView_image);
        nameTextView = findViewById(R.id.textView_name);
        scoreTextView = findViewById(R.id.textView_score);
        nextButton = findViewById(R.id.button_next);
        commentEditText = findViewById(R.id.editText_comment);
        tag = findViewById(R.id.old_new_tag);
        surroundingImageView=findViewById(R.id.view_surrounding_image);

        qrCode = (QRCode) getIntent().getSerializableExtra("qrCode");
        scanned = (boolean) getIntent().getBooleanExtra("scanned", false);
        userName = (String) getIntent().getStringExtra("Username");



        qrImageTextView.setText(qrCode.getFace());
        nameTextView.setText(qrCode.getName());
        scoreTextView.setText("Score: " + qrCode.getScore());
        surroundingImageView.setImageBitmap(qrCode.getLocationImageBitmap());
        HashMap<String, String> commentsMap = qrCode.getComments();

        if (scanned) {
            tag.setText("Already Owned!");
            commentEditText.setText((CharSequence) commentsMap.get(userName));
            commentEditText.setEnabled(false);
        } else {
            tag.setText("New QR Code!");
        }


        nextButton.setOnClickListener(view -> {
            if (!scanned) {
                String comment = String.valueOf(commentEditText.getText());
                qrCode.addComments(userName, comment);
                QRDatabaseController dbAdapter = QRDatabaseController.getInstance();
                dbAdapter.pushQR(qrCode);
            }
            finish();
        });
    }


}

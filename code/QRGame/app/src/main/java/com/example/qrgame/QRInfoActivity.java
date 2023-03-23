package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Displays all the information of a QR code to the user
 */
public class QRInfoActivity extends AppCompatActivity {

    private TextView qrImageTextView;
    private TextView nameTextView;
    private TextView scoreTextView;
    private QRCode qrCode;
    private Button nextButton;

    private final int PICTURE_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_info_page);

        qrImageTextView = findViewById(R.id.textView_image);
        nameTextView = findViewById(R.id.textView_name);
        scoreTextView = findViewById(R.id.textView_score);
        nextButton = findViewById(R.id.button_next);


        qrCode = (QRCode) getIntent().getSerializableExtra("qrCode");

        qrImageTextView.setText(qrCode.getFace());
        nameTextView.setText(qrCode.getName());
        scoreTextView.setText("Score: " + qrCode.getScore());
        Intent surroundingsPictureActivity = new Intent(this, PromptUserPictureActivity.class);

        nextButton.setOnClickListener(view -> {
            startActivityForResult(surroundingsPictureActivity, PICTURE_REQUEST);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK) {
            byte[] bytes = (byte[]) data.getExtras().get("bytes");
            qrCode.setLocation_image(bytes.toString());
        }
        QRDatabaseController dbAdapter = QRDatabaseController.getInstance();
        dbAdapter.pushQR(qrCode);

        finish();
    }
}

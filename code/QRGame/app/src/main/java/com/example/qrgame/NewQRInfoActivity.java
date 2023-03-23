package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Displays all the information of a QR code to the user
 */
public class NewQRInfoActivity extends AppCompatActivity {

    private TextView qrImageTextView;
    private TextView nameTextView;
    private TextView scoreTextView;
    private EditText commentEditText;
    private QRCode qrCode;
    private Button nextButton;

    private final int PICTURE_REQUEST = 1;

    private String comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_qr_info_page);

        qrImageTextView = findViewById(R.id.textView_image);
        nameTextView = findViewById(R.id.textView_name);
        scoreTextView = findViewById(R.id.textView_score);
        nextButton = findViewById(R.id.button_next);
        commentEditText = findViewById(R.id.editText_comment);


        qrCode = (QRCode) getIntent().getSerializableExtra("qrCode");

        qrImageTextView.setText(qrCode.getFace());
        nameTextView.setText(qrCode.getName());
        scoreTextView.setText("Score: " + qrCode.getScore());
        Intent surroundingsPictureActivity = new Intent(this, PromptUserPictureActivity.class);

        nextButton.setOnClickListener(view -> {
            comment = String.valueOf(commentEditText.getText());
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
        qrCode.addComments("test", comment); // TODO - Set to username instead of test
        QRDatabaseController dbAdapter = QRDatabaseController.getInstance();
        dbAdapter.pushQR(qrCode);

        finish();
    }
}

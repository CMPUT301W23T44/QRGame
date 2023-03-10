package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class QRInfoActivity extends AppCompatActivity {

    private TextView qrImageTextView;
    private TextView nameTextView;
    private TextView scoreTextView;
    private QRCode qrCode;
    private Button nextButton;

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
        scoreTextView.setText("Score: " + String.valueOf(qrCode.getScore()));
        Intent surroundingsPictureActivity = new Intent(this, CameraActivitySurrounding.class);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(surroundingsPictureActivity);
            }
        });



    }
}

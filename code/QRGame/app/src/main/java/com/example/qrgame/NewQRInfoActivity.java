package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

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

        qrImageTextView = findViewById(R.id.textView_image);
        nameTextView = findViewById(R.id.textView_name);
        scoreTextView = findViewById(R.id.textView_score);
        nextButton = findViewById(R.id.button_next);
        commentEditText = findViewById(R.id.editText_comment);
        locationCheckbox = findViewById(R.id.checkBox_geolocation);


        qrCode = (QRCode) getIntent().getSerializableExtra("qrCode");
        userName = (String) getIntent().getStringExtra("Username");


        qrImageTextView.setText(qrCode.getFace());
        nameTextView.setText(qrCode.getName());
        scoreTextView.setText("Score: " + qrCode.getScore());
        Intent surroundingsPictureActivity = new Intent(this, PromptUserPictureActivity.class);

        nextButton.setOnClickListener(view -> {
            comment = String.valueOf(commentEditText.getText());
            if (locationCheckbox.isChecked()) {

            }
            startActivityForResult(surroundingsPictureActivity, PICTURE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            byte[] bytes = (byte[]) data.getExtras().get("bytes");
            qrCode.setLocation_image(Base64.getEncoder().encodeToString(bytes));
        }
        qrCode.addComments(userName, comment);
        QRDatabaseController dbAdapter = QRDatabaseController.getInstance();
        dbAdapter.pushQR(qrCode);
        addQR(qrCode);
        finish();
    }

    /**
     * return the AndroidID
     *
     * @return
     */
    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }

    /**
     * return the deviceId
     *
     * @return
     */
    public String getUdid() {
        String androidID = AndroidID();
        return "2" + UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-", "");
    }
    /**
     * add QRcode in UserCollection
     * @param qrCode
     */
    public void addQR(QRCode qrCode){
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fireStore.collection("LoginUser").document(getUdid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Username = document.getString("UserNameKey");
                        DocumentReference docRef2 = fireStore.collection("UserCollection").document(Username);
                        docRef2.update("QRCode", FieldValue.arrayUnion(qrCode));

                    }
                }
            }
        });
    }
}

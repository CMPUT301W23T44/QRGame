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

import java.util.Base64;
import java.util.UUID;

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

        qrImageTextView = findViewById(R.id.textView_image);
        nameTextView = findViewById(R.id.textView_name);
        scoreTextView = findViewById(R.id.textView_score);
        nextButton = findViewById(R.id.button_next);
        commentEditText = findViewById(R.id.editText_comment);
        locationCheckbox = findViewById(R.id.checkBox_geolocation);

        // Gets new QR code and current user logged in
        qrCode = (QRCode) getIntent().getSerializableExtra("qrCode");
        userName = (String) getIntent().getStringExtra("Username");

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
                longitude = (Double) getIntent().getDoubleExtra("longitude", 0);
                latitude = (Double) getIntent().getDoubleExtra("latitude", 0);
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
        addQR(qrCode);
        finish();
    }

    /**
     * Return the AndroidID
     * @return
     *      Android ID of the current phone
     */
    private String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }

    /**
     * Return the deviceId
     * @return
     *      Device ID of current phone
     */
    private String getUdid() {
        String androidID = AndroidID();
        return "2" + UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-", "");
    }
    /**
     * Add QR code in UserCollection
     * @param qrCode - QR code to be added to the database
     */
    private void addQR(QRCode qrCode){
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

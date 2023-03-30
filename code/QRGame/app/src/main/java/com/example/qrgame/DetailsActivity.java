package com.example.qrgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class DetailsActivity extends AppCompatActivity {

    private  QRCode qrcode;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        TextView qrname=findViewById(R.id.details_name);
        TextView qrScore=findViewById(R.id.details_score);
        TextView qrFace=findViewById(R.id.details_qr_face);
        ImageView image=findViewById(R.id.details_surrounding_image);


        Button back=findViewById(R.id.details_back_button);
        Button delete=findViewById(R.id.detail_delete_button);


        qrcode = (QRCode) getIntent().getSerializableExtra("qrCode");
        userName = (String) getIntent().getStringExtra("Username");

        qrname.setText(qrcode.getName());
        qrScore.setText("Score:"+qrcode.getScore());
        qrFace.setText(qrcode.getFace());
        image.setImageBitmap(qrcode.getLocationImageBitmap());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DetailsActivity.this,Inventory_activity.class);
                startActivity(intent);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQR(qrcode);
                qrcode.removeUsers(userName);
                QRDatabaseController dbAdapter=QRDatabaseController.getInstance();
                dbAdapter.pushQR(qrcode);

                Intent intent =new Intent(DetailsActivity.this,Inventory_activity.class);
                startActivity(intent);
            }
        });


    }








    public void deleteQR(QRCode qrCode){
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fireStore.collection("LoginUser").document(getUdid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Username = document.getString("UserNameKey");
                        DocumentReference docRef2 = fireStore.collection("UserCollection").document(Username);
                        docRef2.update("QRCode", FieldValue.arrayRemove(qrCode));

                    }
                }
            }

        });
    }



    /**
     * Gets device Id
     * @return
     * Device Id as a String
     */
    public String getUdid() {
        String androidID=AndroidID();
        return"2"+ UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-","");
    }


    /**
     * Gets Android Id
     * @return
     * Android Id as a String
     */
    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }
}